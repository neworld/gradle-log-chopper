package lt.neworld.logchopper

import com.github.ajalt.mordant.TermColors
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.InputStream

class Processor(private val input: InputStream, private val output: File?, val filter: String? = null) {
    fun run() {
        if (output != null && output.isFile) {
            throw IllegalArgumentException("output ${output.path} is input")
        }

        output?.mkdirs()

        val inputStream = input
        val splitter = Splitter(filter)
        runBlocking {
            var index = 0
            for (chunk in splitter.split(inputStream)) {
                val outputStream = if (output != null) {
                    val name = "${index.toString().padStart(4, '0')}-${chunk.name}.txt"
                    File(output, name).outputStream()
                } else {
                    with(TermColors()) {
                        println((bold)("${chunk.name}:"))
                    }
                    System.out
                }

                outputStream.use {
                    chunk.input.copyTo(it)
                }
                index++
            }
        }
    }
}
