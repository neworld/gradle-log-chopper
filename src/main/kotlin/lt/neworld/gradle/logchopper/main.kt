package lt.neworld.gradle.logchopper

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import java.io.File

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::Args).run {
        Processor(file, output).run()
    }
}

class Args(parser: ArgParser) {
    val output: File by parser.storing(
            "-o", "--output",
            help = "Output dir",
            transform = { File(this) }
    ).default(File("out"))

    val file: File by parser.positional("INPUT", "Gradle log output.") {
        File(this)
    }
}
