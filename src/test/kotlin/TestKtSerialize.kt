import kotlinx.serialization.json.Json
import moe.kurenai.cq.event.BasicEvent
import moe.kurenai.cq.event.PrivateMessageEvent
import moe.kurenai.cq.event.PrivateMessageEventSubType
import org.junit.jupiter.api.Test

class TestKtSerialize {

    private val JSON = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun testJson() {
        val event = PrivateMessageEvent(1000L, 1L, 1, 1L, emptyList(), "", 1, PrivateMessageEventSubType.GROUP)
        println(event.postType)
        val json = JSON.encodeToJsonElement(PrivateMessageEvent.serializer(), event)
        println(json)
        val basic = JSON.decodeFromJsonElement(BasicEvent.serializer(), json)
        println(basic)
    }
}