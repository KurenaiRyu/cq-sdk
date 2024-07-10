import moe.kurenai.cq.util.DefaultMapper.MAPPER

/**
 * @author Kurenai
 * @since 6/29/2022 15:55:54
 */

suspend fun main() {
    val json = "{\"data\":{\"message_id\":1208264637},\"echo\":\"1\",\"retcode\":0,\"status\":\"ok\"}"
    val jsonNode = MAPPER.readTree(json)
    println(jsonNode.findValue("echo"))

}