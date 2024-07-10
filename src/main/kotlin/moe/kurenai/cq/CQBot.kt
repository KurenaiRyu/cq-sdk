package moe.kurenai.cq

import io.vertx.core.http.WebSocketClient
import io.vertx.kotlin.coroutines.CoroutineVerticle
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
import org.apache.logging.log4j.LogManager

class CQBot (
    val host: String,
    val port: Int,
    val token: String?,
    jsonBuilder: JsonBuilder.() -> Unit
): CoroutineVerticle() {

    companion object {
        private val log = LogManager.getLogger()
    }

    private val json: Json = Json(builderAction = jsonBuilder)
    private lateinit var client: WebSocketClient

    override suspend fun start() {
        client = vertx.createWebSocketClient()

        client.connect("http://$host:$port") { result ->
            val ws = result.result()
            ws.textMessageHandler { text ->
                launch {
                    handleMessage(text)
                }
            }
        }
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