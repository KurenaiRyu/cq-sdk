import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.seconds

class TestContinuation {

    @Test
    fun test(): Unit = runBlocking {

        try {
            val result =
                withTimeout(1.seconds) {
                    suspendCancellableCoroutine { con ->
                        con.invokeOnCancellation {
                            println("Canceled")
                        }
                        CoroutineScope(Dispatchers.Default).launch {
                            println("Delay 2000")
                            delay(2000)
                            println("return, active: ${con.isActive}")
                            con.resume(1)
                        }
                        println("After launch")
                        CoroutineScope(Dispatchers.Default).launch {
                            delay(1000)
                            con.cancel(CancellationException("Time out"))
                        }
                    }
                }
            println("after suspend")
            println(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        delay(2000)
        println("final")
    }
}