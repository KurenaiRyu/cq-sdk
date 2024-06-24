import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import moe.kurenai.cq.CoroutineCQBot
import org.junit.jupiter.api.Test

class TestBot {
    @Test
    fun test() = runBlocking {
        val bot = CoroutineCQBot(apiHost = "10.0.1.2")
        bot.start()
        bot.messageChannel.consumeEach {
            println(it)
        }
    }
}