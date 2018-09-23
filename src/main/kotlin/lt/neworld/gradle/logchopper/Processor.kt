package lt.neworld.gradle.logchopper

import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.PipedInputStream

class Processor(private val input: File, private val output: File) {
    fun run() {
        val inputStream = input.inputStream()
        val splitter = Splitter()
        runBlocking {
            for (chunk in splitter.split(inputStream)) {
                println("get new chunk: ${chunk.name}")
                File(output, "${chunk.name}.txt").outputStream().use {
                    val reader = PipedInputStream()
                    chunk.output.connect(reader)
                    reader.copyTo(it)
                }
                println("Finish chunk saving")
            }
        }
    }
}
