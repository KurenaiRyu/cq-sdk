package moe.kurenai.cq.uritl

import oshi.SystemInfo
import oshi.software.os.OSProcess
import oshi.software.os.OperatingSystem


/**
 * @author Kurenai
 * @since 7/21/2022 23:48:37
 */

object ProcessUtil {

    private var previousTime = 0L

    fun printInfo(pid: Int) {
        diskUtilizationPerProcess(pid)
        cpuUtilizationPerProcess(pid)
        memoryUtilizationPerProcess(pid)
    }

    fun diskUtilizationPerProcess(pid: Int) {
        /**
         * ByteRead : Returns the number of bytes the process has read from disk.
         * ByteWritten : Returns the number of bytes the process has written to disk.
         */
        val process: OSProcess
        val si = SystemInfo()
        val os: OperatingSystem = si.operatingSystem
        process = os.getProcess(pid)
        println("\nDisk I/O Usage : ")
        println("I/O Reads: " + process.bytesRead)
        println("I/O Writes: " + process.bytesWritten)
    }

    fun cpuUtilizationPerProcess(processId: Int) {
        /**
         * User Time : Returns the number of milliseconds the process has executed in user mode.
         * Kernel Time : Returns the number of milliseconds the process has executed in kernel/system mode.
         */
        val systemInfo = SystemInfo()
        val processor = systemInfo.hardware.processor
        val cpuNumber = processor.logicalProcessorCount
        //int processId = systemInfo.getOperatingSystem().getProcessId();
        val process = systemInfo.operatingSystem.getProcess(processId)
        val currentTime = process.kernelTime + process.userTime
        val timeDifference: Long = currentTime - previousTime
        val processCpu = 100 * (timeDifference / 5000.0) / cpuNumber
        previousTime = currentTime
        println("\nCPU Usage :")
        println("CPU : " + processCpu.toInt() + "%")
    }

    fun memoryUtilizationPerProcess(pid: Int) {
        /**
         * Resident Size : how much memory is allocated to that process and is in RAM
         */
        val process: OSProcess
        val si = SystemInfo()
        val os = si.operatingSystem
        process = os.getProcess(pid)
        println("\nMemory Usage :")
        println("Resident Size: ${process.residentSetSize}(${process.residentSetSize shr 20}M)")
    }

}