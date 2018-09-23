package lt.neworld.gradle.logchopper

import kotlinx.coroutines.runBlocking
import java.io.File

class Processor(private val input: File, private val output: File) {
    fun run() {
        if (output.isFile) {
            throw IllegalArgumentException("output ${output.path} is file")
        }

        output.mkdirs()

        val inputStream = input.inputStream()
        val splitter = Splitter()
        runBlocking {
            for (chunk in splitter.split(inputStream)) {
                File(output, "${chunk.name}.txt").outputStream().use {
                    chunk.input.copyTo(it)
                }
            }
        }
    }
}
