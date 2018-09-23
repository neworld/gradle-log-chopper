package lt.neworld.gradle.logchopper

import lt.neworld.kupiter.testFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File

class Test {
    private val testData = File("src/test/testData")

    @RegisterExtension
    @JvmField
    val temporaryFolder = TemporaryFolderExtension()

    @TestFactory
    fun allTests() = testFactory {
        testData.listFiles().forEach { testDir ->
            test(testDir.name) {
                val input = File(testDir, INPUT_FILE)
                val output = temporaryFolder.folder
                Processor(input, output).run()

                testDir.listFiles().filter(::filterNonTestFiles).forEach {
                    val fileName = it.name
                    val outputFile = File(output, fileName)

                    assertTrue(outputFile.exists(), "Processor didn't create $fileName in $output")
                    assertEquals(it.readText(), outputFile.readText(), "Content is not the same")
                }

                val allRequiredFileNames = testDir.list().filter(::filterNonTestFiles)
                val allUnwantedFiles = output.listFiles().filter {
                    !allRequiredFileNames.contains(it.name)
                }

                assertTrue(allUnwantedFiles.isEmpty()) {
                    "Founded: " + allUnwantedFiles.map { it.name } + "\n"
                    "Content: \n\n" + allUnwantedFiles.map { it.name + ":\n" + it.readText() + "\n\n-------------\n" }
                }
            }
        }
    }

    private fun filterNonTestFiles(name: String) = name != INPUT_FILE && !name.startsWith(".")

    private fun filterNonTestFiles(file: File) = filterNonTestFiles(file.name)

    companion object {
        private const val INPUT_FILE = "input.txt"
    }
}
