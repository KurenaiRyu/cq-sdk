import moe.kurenai.cq.event.MessageEvent
import moe.kurenai.cq.event.group.GroupMessageEvent
import moe.kurenai.cq.model.Member
import moe.kurenai.cq.util.DefaultMapper
import org.junit.jupiter.api.Test

/**
 * @author Kurenai
 * @since 6/30/2022 11:27:48
 */

class JacksonTest {

    @Test
    fun testSubType() {
        val event = GroupMessageEvent(
            0, 0, 0, 0, emptyList(), "", 0, 0,
            Member(0, "", "", "", 0, "", "", "", ""), ""
        )
        val mapper = DefaultMapper.MAPPER
        val json = mapper.writeValueAsString(event)
        println(json)
        val readValue = mapper.readValue(json, MessageEvent::class.java)
        println(readValue)
    }
}