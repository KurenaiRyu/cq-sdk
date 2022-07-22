package moe.kurenai.cq.uritl

import java.util.concurrent.atomic.AtomicLong

/**
 * @author Kurenai
 * @since 7/19/2022 17:28:39
 */

object IdGenerator {

    private val currId = AtomicLong(0)

    fun nextId(): Long {
        return currId.updateAndGet {
            if (it == Long.MAX_VALUE) 0
            else it + 1
        }
    }

    fun nextStr(): String = nextId().toString()


}