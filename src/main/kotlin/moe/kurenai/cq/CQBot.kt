package moe.kurenai.cq

import io.vertx.core.http.WebSocketClient
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import moe.kurenai.cq.event.PostType
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
        jsonObj[PostType.FIELD_NAME]?.jsonPrimitive?.
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