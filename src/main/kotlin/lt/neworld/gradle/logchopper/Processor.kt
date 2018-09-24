package lt.neworld.gradle.logchopper

import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.InputStream

class Processor(private val input: InputStream, private val output: File, val filter: String? = null) {
    fun run() {
        if (output.isFile) {
            throw IllegalArgumentException("output ${output.path} is input")
        }

        output.mkdirs()

        val inputStream = input
        val splitter = Splitter(filter)
        runBlocking {
            var index = 0
            for (chunk in splitter.split(inputStream)) {
                File(output, "${index.toString().padStart(4, '0')}-${chunk.name}.txt").outputStream().use {
                    chunk.input.copyTo(it)
                }
                index++
            }
        }
    }
}
