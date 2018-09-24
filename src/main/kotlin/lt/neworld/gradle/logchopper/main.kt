package lt.neworld.gradle.logchopper

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import java.io.File
import java.io.InputStream
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = mainBody(programName = "gradle-logchopper") {
    val help = DefaultHelpFormatter(
            prologue = "Example: ./gradlew build --debug > gradle-logchopper"
    )

    ArgParser(args, helpFormatter = help).parseInto(::Args).run {
        val time = measureTimeMillis {
            Processor(input, output, filter).run()
        }

        println("Chopped in $time ms")
    }
}

class Args(parser: ArgParser) {
    val filter: String? by parser.storing(
            "-f", "--filter",
            help = "Output logs filtered by given text"
    ).default(null as String?)

    val output: File by parser.storing(
            "-o", "--output",
            help = "Output dir. Default ./firewood/",
            transform = { resolveFile(this) }
    ).default(File("firewood"))

    val input: InputStream by parser.positional("INPUT", "Gradle log output. Leave empty to read from stdin") {
        resolveFile(this).inputStream()
    }.default(System.`in`)

    private fun resolveFile(path: String): File {
        val realPath = if (path.startsWith("~")) {
            System.getProperty("user.home") + path.drop(1)
        } else {
            path
        }

        return File(realPath)
    }
}
