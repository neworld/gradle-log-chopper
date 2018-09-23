package lt.neworld.gradle.logchopper

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

class Splitter {
    suspend fun split(input: InputStream): Channel<ChunkMetaData> {
        val channel = Channel<ChunkMetaData>()

        GlobalScope.launch(context = newSingleThreadContext("Splitter")) {
            process(input, channel)
        }
        return channel
    }

    private suspend fun process(input: InputStream, channel: Channel<ChunkMetaData>) {
        suspend fun createNewChunk(name: String): OutputStream {
            val inputStream = PipedInputStream()
            val outputStream = PipedOutputStream(inputStream)
            channel.send(ChunkMetaData(name, inputStream))
            return outputStream
        }

        var currentOutput: OutputStream = createNewChunk("default")

        for (line in input.bufferedReader().lines()) {
            if (line.contains("[LIFECYCLE]") && line.contains("> Task")) {
                val name = line.substringAfterLast("> Task").trim().substringBefore(" ")
                currentOutput.close()
                currentOutput = createNewChunk(name)
            }

            currentOutput.write(line.toByteArray())
            currentOutput.write("\n".toByteArray())
        }
        currentOutput.close()
        channel.close()
    }
}
