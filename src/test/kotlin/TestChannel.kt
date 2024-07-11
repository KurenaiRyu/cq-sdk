import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.Test

class TestChannel {

    @Test
    fun test(): Unit = runBlocking {
        val channel = Channel<Int>(5)
        CoroutineScope(Dispatchers.Default).launch {
            for (i in 0..50) {
                channel.send(i)
                println("Send $i")
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            for (i in channel) {
                println("Receiver 1 $i")
                delay(1000)
            }
//            for (i in 0..1000) {
////                Assertions.assertEquals(i, channel.receive(), "Receiver 1 not match.")
//                println("Receiver 1 ${channel.receive()}")
//                delay(1000)
//            }
        }

        val job = CoroutineScope(Dispatchers.Default).launch {
            for (i in channel) {
                println("Receiver 2 $i")
                delay(1000)
            }
//            for (i in 0..1000) {
////                Assertions.assertEquals(i, channel.receive(), "Receiver 2 not match.")
//                println("Receiver 2 ${channel.receive()}")
//                delay(1000)
//            }
        }

        job.join()
    }
}