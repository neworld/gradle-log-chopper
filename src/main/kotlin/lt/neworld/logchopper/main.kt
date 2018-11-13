package lt.neworld.logchopper

import com.github.ajalt.mordant.TermColors
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.DefaultHelpFormatter
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import java.io.File
import java.io.InputStream
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = mainBody(programName = "logchopper") {
    val help = DefaultHelpFormatter(
            prologue = "Example: ./gradlew build --debug > logchopper"
    )

    ArgParser(args, helpFormatter = help).parseInto(::Args).run {
        val time = measureTimeMillis {
            Processor(input, output.takeUnless { print }, filter).run()
        }

        with(TermColors()) {
            println((gray + italic)("\nChopped in $time ms"))
        }
    }
}

class Args(parser: ArgParser) {
    val print: Boolean by parser.flagging(
            "--print", "-p",
            help = "Output to stdout instead of writing into files. Useful if you filter single lifecycle event"
    )

    val filter: String? by parser.storing(
            "-f", "--filter",
            help = "Output logs filtered by given text. Glob syntax is supported"
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
