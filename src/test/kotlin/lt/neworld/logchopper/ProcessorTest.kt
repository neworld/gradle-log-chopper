package lt.neworld.logchopper

import lt.neworld.kupiter.testFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File

class ProcessorTest {
    private val testData = File("src/test/testData")

    @RegisterExtension
    @JvmField
    val temporaryFolder = TemporaryFolderExtension()

    @TestFactory
    fun allTests() = testFactory {
        testData.listFiles().forEach { testDir ->
            test(testDir.name) {
                try {
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
                } finally {
                    cleanup()
                }
            }
        }
    }

    @Nested
    inner class Filter {
        val input = File(testData, "MultipleLifecycle/input.txt")
        val output by lazy { temporaryFolder.folder }

        @Test
        fun noFilter() {
            Processor(input, output, filter = "").run()

            val expected = setOf(
                    "0000-default.txt",
                    "0001-:buildSrc:processResources.txt",
                    "0002-:buildSrc:inspectClassesForKotlinIC.txt"
            )

            val actual = output.listFiles().map { it.name }.toSet()

            assertEquals(expected, actual)
        }

        @Test
        fun withDefault() {
            Processor(input, output, filter = "default").run()

            val expected = setOf(
                    "0000-default.txt"
            )

            val actual = output.listFiles().map { it.name }.toSet()

            assertEquals(expected, actual)
        }

        @Test
        fun withBuildSrc() {
            Processor(input, output, filter = "buildSrc").run()

            val expected = setOf(
                    "0000-:buildSrc:processResources.txt",
                    "0001-:buildSrc:inspectClassesForKotlinIC.txt"
            )

            val actual = output.listFiles().map { it.name }.toSet()

            assertEquals(expected, actual)
        }

        @Test
        fun matchCase() {
            Processor(input, output, filter = "Resources").run()

            val expected = setOf(
                    "0000-:buildSrc:processResources.txt"
            )

            val actual = output.listFiles().map { it.name }.toSet()

            assertEquals(expected, actual)
        }
    }

    private fun cleanup() {
        temporaryFolder.folder.deleteRecursively()
        temporaryFolder.folder.mkdir()
    }

    private fun filterNonTestFiles(name: String) = name != INPUT_FILE && !name.startsWith(".")

    private fun filterNonTestFiles(file: File) = filterNonTestFiles(file.name)

    companion object {
        private const val INPUT_FILE = "input.txt"
    }
}
