package moe.kurenai.cq

import io.netty.handler.codec.http.HttpHeaderNames
import io.vertx.core.http.WebSocketClient
import io.vertx.core.http.WebSocketConnectOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import moe.kurenai.cq.event.*
import moe.kurenai.cq.event.group.GroupIncreaseEvent
import moe.kurenai.cq.event.group.GroupMessageEvent
import moe.kurenai.cq.event.group.GroupRecallEvent
import moe.kurenai.cq.event.group.GroupUploadFileEvent
import moe.kurenai.cq.model.BasicResponse
import moe.kurenai.cq.request.Request
import moe.kurenai.cq.request.RequestWrapper
import moe.kurenai.cq.request.wrap
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.time.Duration.Companion.seconds

class CQBot(
    val host: String,
    val port: Int,
    val token: String?,
    jsonBuilder: JsonBuilder.() -> Unit
) : CoroutineVerticle() {

    companion object {
        private val log: Logger = LogManager.getLogger()
    }

    private val json: Json = Json(builderAction = jsonBuilder)
    private lateinit var client: WebSocketClient

    private val eventChannel = Channel<Event>(capacity = 500) {
        log.warn("Event suspend by channel full: {}", it)
    }

    private val handlers = mutableListOf<EventHandler>()
    private val requestConMap = mutableMapOf<String, CancellableContinuation<Any>>()

    public override suspend fun start() {

        client = vertx.createWebSocketClient()

        client.connect(WebSocketConnectOptions().apply {
            uri = "ws://$host:$port"
            if (token?.isNotEmpty() == true) {
                addHeader(HttpHeaderNames.AUTHORIZATION, "Bearer $token")
            }
        }) { result ->

            val ws = result.result()

            ws.textMessageHandler { text ->
                launch {
                    handleMessage(text)
                }
            }
        }

        this.launch {
            for (event in eventChannel) {
                for (handler in handlers) {
                    try {
                        handler.handle(event)
                    } catch (e: Exception) {
                        log.warn("Handle event error", e)
                    }
                }
            }
        }
    }

    suspend fun <T> send(kSerializer: KSerializer<RequestWrapper<Request<T>>>, request: Request<T>): T {
        val wrapper: RequestWrapper<Request<T>>  = request.wrap()
        val requestJson = json.encodeToString(
            kSerializer,
            wrapper)
        log.debug("Request: {}", requestJson)

        return withTimeout(5.seconds) {
            suspendCancellableCoroutine { con ->
                requestConMap[wrapper.echo] = con as CancellableContinuation<Any>
                con.invokeOnCancellation {
                    requestConMap.remove(wrapper.echo)
                }
                client.webSocket().writeFinalTextFrame(requestJson)
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    suspend inline fun <reified T: Any> send(request: Request<T>): T {
        return send(RequestWrapper.serializer(Request.serializer(T::class.serializer())), request)
    }

    fun addHandler(handler: EventHandler): EventHandler {
        if (!handlers.contains(handler)) handlers.add(handler)
        return handler
    }

    fun removeHandler(handler: EventHandler): Boolean {
        return handlers.remove(handler)
    }

    fun EventHandler.remove(): Boolean {
        return removeHandler(this)
    }

    private suspend fun handleMessage(text: String) {
        log.debug("Incoming: ${text.trim()}")
        val jsonObject = json.parseToJsonElement(text).jsonObject
        if (jsonObject["echo"] == null) {
            handleResponse(jsonObject)
        } else {
            handleEvent(jsonObject)
        }
    }

    private fun handleResponse(jsonObj: JsonObject) {
        val basicResponse = json.decodeFromJsonElement(BasicResponse.serializer(), jsonObj)
        val echo = basicResponse.echo?:return
        requestConMap[echo]?.resumeWith(Result.success(jsonObj))
    }

    private suspend fun handleEvent(jsonObj: JsonObject) {
        val basicEvent = json.decodeFromJsonElement(BasicEvent.serializer(), jsonObj)
        val event = when (basicEvent.postType) {
            PostType.MESSAGE -> {
                when (basicEvent.messageType!!) {
                    MessageEventType.PRIVATE -> json.decodeFromJsonElement(PrivateMessageEvent.serializer(), jsonObj)
                    MessageEventType.GROUP -> json.decodeFromJsonElement(GroupMessageEvent.serializer(), jsonObj)
                }
            }

            PostType.NOTICE -> {
                when (basicEvent.noticeType!!) {
                    NoticeType.GROUP_RECALL -> json.decodeFromJsonElement(GroupRecallEvent.serializer(), jsonObj)
                    NoticeType.GROUP_INCREASE -> json.decodeFromJsonElement(GroupIncreaseEvent.serializer(), jsonObj)
                    NoticeType.GROUP_UPLOAD -> json.decodeFromJsonElement(GroupUploadFileEvent.serializer(), jsonObj)
                }
            }

            PostType.REQUEST -> {
                json.decodeFromJsonElement(RequestEvent.serializer(), jsonObj)
            }

            PostType.META -> {
                json.decodeFromJsonElement(MetaEvent.serializer(), jsonObj)
            }
        }
        eventChannel.send(event)
    }
}

class CQBotBuilder {
    var port: Int = 6800
    var host: String = "localhost"
    var token: String? = null
    var jsonBuilder: JsonBuilder.() -> Unit = {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun build() = CQBot(host = host, port = port, token = token, jsonBuilder = jsonBuilder)
}

class EventHandler(
    val block: suspend (Event) -> Unit
) {
    suspend fun handle(event: Event) {
        block(event)
    }
}