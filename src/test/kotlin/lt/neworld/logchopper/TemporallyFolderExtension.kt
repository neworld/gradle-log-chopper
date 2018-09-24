package lt.neworld.logchopper

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File

class TemporaryFolderExtension : BeforeEachCallback, AfterEachCallback {
    lateinit var folder: File
        private set

    override fun afterEach(extensionContext: ExtensionContext) {
        recursiveDelete(folder)
    }

    override fun beforeEach(extensionContext: ExtensionContext) {
        folder = File.createTempFile("junit", "", null)
        folder.delete()
        folder.mkdir()
    }

    private fun recursiveDelete(file: File) {
        val files = file.listFiles()
        if (files != null) {
            for (each in files) {
                recursiveDelete(each)
            }
        }
        file.delete()
    }

}
