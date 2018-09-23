package lt.neworld.gradle.logchopper

import java.io.PipedOutputStream

data class ChunkMetaData(val name: String, val output: PipedOutputStream)
