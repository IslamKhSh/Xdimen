import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class XdimenPluginTest {

    @TempDir
    lateinit var testProjectDir: File
    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var gradleRunner: GradleRunner

    @BeforeEach
    fun setup() {
        settingsFile = File(testProjectDir, "settings.gradle.kts")
        buildFile = File(testProjectDir, "build.gradle.kts")

        gradleRunner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir)
            .withDebug(true)
    }

    @Test
    fun `test run generateXdimen task`() {
        // given
        writeFile(buildFile, getResourceAsText("/gradle.build.txt"))

        // when
        val result = gradleRunner
            .withArguments("generateXdimen")
            .build()

        // then
        assertEquals(TaskOutcome.SUCCESS, result.task(":deleteXdimen")?.outcome)
        assertEquals(TaskOutcome.SUCCESS, result.task(":generateXdimen")?.outcome)
        println(result.output)
    }

    @Test
    fun `test run generateXdimen task on non android project`() {
        writeFile(buildFile, getResourceAsText("/gradle.build_without_android.txt"))

        assertThrows<Exception> { gradleRunner.build() }
    }

    @Test
    fun `test run generateXdimen task with xdimen config`() {
        // given
        writeFile(buildFile, getResourceAsText("/gradle.build_with_config.txt"))

        // when
        val result = gradleRunner
            .withArguments("generateXdimen")
            .build()

        // then
        assertNull(result.task(":deleteXdimen")?.outcome) // deleteOldXdimen = false
        assertEquals(TaskOutcome.SUCCESS, result.task(":generateXdimen")?.outcome)

        // targetedScreensWidth = phonePortrait - 300 + 310
        assertFalse(result.output.contains("/values-w300dp/xdimen.xml"))
        assertTrue(result.output.contains("/values-w310dp/xdimen.xml"))

        println(result.output)
    }

    @Test
    fun `test run generateXdimen task with invalid ranges in configs`() {
        writeFile(buildFile, getResourceAsText("/gradle.build_with_invalid_ranges.txt"))

        assertThrows<Exception> {
            gradleRunner
                .withArguments("generateXdimen")
                .build()
        }
    }

    @Test
    fun `test run generateXdimen task with invalid range step`() {
        writeFile(buildFile, getResourceAsText("/gradle.build_with_invalid_step.txt"))

        assertThrows<Exception> {
            gradleRunner
                .withArguments("generateXdimen")
                .build()
        }
    }

    @Test
    fun `test run generateXdimen task with invalid designWidth`() {
        writeFile(buildFile, getResourceAsText("/gradle.build_with_invalid_designWidth.txt"))

        assertThrows<Exception> {
            gradleRunner
                .withArguments("generateXdimen")
                .build()
        }
    }

    private fun getResourceAsText(path: String): String =
        object {}.javaClass.getResource(path)?.readText().orEmpty()

    private fun writeFile(destination: File, content: String) {
        destination.bufferedWriter().use {
            it.write(content)
        }
    }
}
