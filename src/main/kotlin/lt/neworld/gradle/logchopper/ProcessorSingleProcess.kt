package lt.neworld.gradle.logchopper

import java.io.File
import java.io.OutputStream

class ProcessorSingleProcess(private val input: File, private val output: File) {
    fun run() {
        if (output.isFile) {
            throw IllegalArgumentException("output ${output.path} is file")
        }

        output.mkdirs()

        val inputStream = input.inputStream()

        fun createNewChunk(name: String): OutputStream {
            return File(output, "$name.txt").outputStream()
        }

        var currentOutput: OutputStream = createNewChunk("default")

        for (line in inputStream.bufferedReader().lines()) {
            if (line.contains("[LIFECYCLE]") && line.contains("> Task")) {
                val name = line.substringAfterLast("> Task").trim().substringBefore(" ")
                currentOutput.close()
                currentOutput = createNewChunk(name)
            }

            currentOutput.write(line.toByteArray())
            currentOutput.write("\n".toByteArray())
        }
        currentOutput.close()
        inputStream.close()
    }
}
