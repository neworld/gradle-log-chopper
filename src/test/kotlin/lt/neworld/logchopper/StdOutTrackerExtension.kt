package lt.neworld.logchopper

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class StdOutTrackerExtension : BeforeEachCallback, AfterEachCallback {
    lateinit var output: ByteArrayOutputStream
        private set

    private lateinit var origin: PrintStream

    override fun beforeEach(extensionContext: ExtensionContext) {
        origin = System.out
        output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
    }

    override fun afterEach(extensionContext: ExtensionContext) {
        System.setOut(origin)
    }
}
