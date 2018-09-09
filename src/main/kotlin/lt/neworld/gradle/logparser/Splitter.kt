package lt.neworld.gradle.logparser

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedOutputStream

class Splitter {
    suspend fun split(input: InputStream): Channel<ChunkMetaData> {
        val channel = Channel<ChunkMetaData>()

        launch(context = newSingleThreadContext("Splitter")) {
            process(input, channel)
        }

        return channel
    }

    private suspend fun process(input: InputStream, channel: Channel<ChunkMetaData>) {
        suspend fun createNewChunk(name: String): OutputStream {
            println("Create new chunk: $name")
            val outputStream = PipedOutputStream()
            channel.send(ChunkMetaData(name, outputStream))
            return outputStream
        }

        var currentOutput: OutputStream = createNewChunk("default")

        for (line in input.bufferedReader().lines()) {
            if (line.contains("[LIFECYCLE]") && line.contains("> Task")) {
                val name = line.substringAfterLast("> Task").trim().substringBefore(" ")
                currentOutput.close()
                currentOutput = createNewChunk(name)
            }

            println("Write a line to output")
            currentOutput.write(line.toByteArray())
            currentOutput.write("\n".toByteArray())
            println("line processing done")
        }
        println("file processing done")
        currentOutput.close()
        channel.close()
    }
}