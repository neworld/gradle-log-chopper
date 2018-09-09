package lt.neworld.gradle.logparser

import java.io.OutputStream
import java.io.PipedOutputStream

data class ChunkMetaData(val name: String, val output: PipedOutputStream)