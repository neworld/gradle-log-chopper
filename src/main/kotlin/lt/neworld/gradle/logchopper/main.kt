package lt.neworld.gradle.logchopper

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::Args).run {

        val time2 = measureTimeMillis {
            (1..50).forEach {
                ProcessorSingleProcess(file, output).run()
            }
        }

        println("Chopped coroutines in $time2 ms")

        val time = measureTimeMillis {
            (1..50).forEach {
                Processor(file, output).run()
            }
        }

        println("Chopped coroutines in $time ms")
    }
}

class Args(parser: ArgParser) {
    val output: File by parser.storing(
            "-o", "--output",
            help = "Output dir",
            transform = { resolveFile(this) }
    ).default(File("out"))

    val file: File by parser.positional("INPUT", "Gradle log output.") {
        resolveFile(this)
    }

    private fun resolveFile(path: String): File {
        val realPath = if (path.startsWith("~")) {
            System.getProperty("user.home") + path.drop(1)
        } else {
            path
        }

        return File(realPath)
    }
}
