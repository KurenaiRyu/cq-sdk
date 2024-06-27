package moe.kurenai.cq.util

import java.io.Serializable
import java.util.*

/**
 * Snowflake
 * <p>Copy from hutool</p>
 */
class Snowflake(
    workerId: Long = 0,
    dataCenterId: Long = 0,
    epochDate: Date? = null
) : Serializable {
    private val serialVersionUID = 1L
    private var twepoch: Long = 0
    private val workerIdBits = 5L
    private val dataCenterIdBits = 5L
    private val maxWorkerId = 31L
    private val maxDataCenterId = 31L
    private val sequenceBits = 12L
    private val workerIdShift = 12L
    private val dataCenterIdShift = 17L
    private val timestampLeftShift = 22L
    private val sequenceMask = 4095L
    private var workerId: Long = 0
    private var dataCenterId: Long = 0
    private var sequence = 0L
    private var lastTimestamp = -1L

    companion object {
        val INSTANT = Snowflake(1, 1)
    }

    init {
        twepoch = epochDate?.time ?: 1288834974657L
        if (workerId in 0L..31L) {
            if (dataCenterId in 0L..31L) {
                this.workerId = workerId
                this.dataCenterId = dataCenterId
            } else {
                throw IllegalArgumentException(
                    String.format(
                        "datacenter Id can't be greater than {} or less than 0",
                        arrayOf<Any>(31L)
                    )
                )
            }
        } else {
            throw IllegalArgumentException(
                String.format(
                    "worker Id can't be greater than {} or less than 0",
                    arrayOf<Any>(31L)
                )
            )
        }
    }

    fun getWorkerId(id: Long): Long {
        return id shr 12 and 31L
    }

    fun getDataCenterId(id: Long): Long {
        return id shr 17 and 31L
    }

    fun getGenerateDateTime(id: Long): Long {
        return (id shr 22 and 2199023255551L) + twepoch
    }

    @Synchronized
    fun nextId(): Long {
        var timestamp = genTime()
        return if (timestamp < lastTimestamp) {
            throw IllegalStateException(
                String.format(
                    "Clock moved backwards. Refusing to generate id for {}ms",
                    arrayOf<Any>(lastTimestamp - timestamp)
                )
            )
        } else {
            if (lastTimestamp == timestamp) {
                sequence = sequence + 1L and 4095L
                if (sequence == 0L) {
                    timestamp = tilNextMillis(lastTimestamp)
                }
            } else {
                sequence = 0L
            }
            lastTimestamp = timestamp
            timestamp - twepoch shl 22 or (dataCenterId shl 17) or (workerId shl 12) or sequence
        }
    }

    fun nextIdStr(): String {
        return nextId().toString()
    }

    private fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp: Long
        timestamp = genTime()
        while (timestamp <= lastTimestamp) {
            timestamp = genTime()
        }
        return timestamp
    }

    private fun genTime(): Long {
        return System.currentTimeMillis()
    }
}