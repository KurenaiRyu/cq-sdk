package moe.kurenai.cq

import io.netty.handler.codec.http.HttpHeaderNames
import io.vertx.core.http.WebSocketClient
import io.vertx.core.http.WebSocketConnectOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import moe.kurenai.cq.event.*
import moe.kurenai.cq.event.group.GroupIncreaseEvent
import moe.kurenai.cq.event.group.GroupMessageEvent
import moe.kurenai.cq.event.group.GroupRecallEvent
import moe.kurenai.cq.event.group.GroupUploadFileEvent
import moe.kurenai.cq.model.BasicResponse
import org.apache.logging.log4j.LogManager
import kotlin.coroutines.Continuation

class CQBot(
    val host: String,
    val port: Int,
    val token: String?,
    jsonBuilder: JsonBuilder.() -> Unit
) : CoroutineVerticle() {

    companion object {
        private val log = LogManager.getLogger()
    }

    private val json: Json = Json(builderAction = jsonBuilder)
    private lateinit var client: WebSocketClient

    private val eventChannel = Channel<Event>(capacity = 500) {
        log.warn("Event suspend by channel full: {}", it)
    }

    private val handlers = mutableListOf<EventHandler>()
    private val requestConMap = mutableMapOf<String, Continuation<*>>()

    override suspend fun start() {

        client = vertx.createWebSocketClient()

        client.connect(WebSocketConnectOptions().apply {
            uri = "ws://$host:$port"
            addHeader(HttpHeaderNames.AUTHORIZATION, "Bearer $token")
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

    public fun addHandler(handler: EventHandler): Boolean {
        return if (!handlers.contains(handler)) handlers.add(handler) else false
    }

    public fun removeHandler(handler: EventHandler): Boolean {
        return handlers.remove(handler)
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

    private suspend fun handleResponse(jsonObj: JsonObject) {
        val basicResponse = json.decodeFromJsonElement(BasicResponse.serializer(), jsonObj)
        val echo = basicResponse.echo?:return
        requestConMap[echo]?.let { con ->
            con.resumeWith(Result.success(jsonObj))
        }
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
    private var port: Int = 6800
    private var host: String = "localhost"
    private var token: String? = null
    private var jsonBuilder: JsonBuilder.() -> Unit = {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun build() = CQBot(host = host, port = port, token = token, jsonBuilder = jsonBuilder)
}

class EventHandler(
    val block: (Event) -> Unit
) {
    fun handle(event: Event) {
        block(event)
    }
}