package tasks.deleteXdimen

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import utils.Constants.VALUES_ALTER_RES_DIR_PREFIX
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

internal class XdimenResRemoverHelperTest {

    @TempDir
    lateinit var projectDir: Path
    private lateinit var xdimenResRemoverHelper: XdimenResRemoverHelper

    @BeforeEach
    fun setup() {
        xdimenResRemoverHelper = XdimenResRemoverHelper()
    }

    @Test
    fun `test delete file in values default config res dir`() {
        // given
        val xdimenResFile = getXdimenResFile("values/xdimen.xml")

        // when
        xdimenResRemoverHelper.getAndRemoveXdimenRes(projectDir.toFile())

        // assert
        assertFalse(xdimenResFile.exists())
        assertTrue(xdimenResRemoverHelper.isAnyFileDeleted)
    }

    @Test
    fun `test delete file in values alternative config res dir`() {
        // given
        val xdimenResFile = getXdimenResFile("${VALUES_ALTER_RES_DIR_PREFIX}333dp/xdimen.xml")

        // when
        xdimenResRemoverHelper.getAndRemoveXdimenRes(projectDir.toFile())

        // assert
        assertFalse(xdimenResFile.exists())
        assertTrue(xdimenResRemoverHelper.isAnyFileDeleted)
    }

    @Test
    fun `test delete dir if it contains xdimen file only`() {
        // given
        getXdimenResFile("values/otherFile.txt")
        val dirWithManyFiles = getXdimenResFile("values/xdimen.xml").parent
        val dirWithOnlyXdimen = getXdimenResFile("${VALUES_ALTER_RES_DIR_PREFIX}333dp/xdimen.xml").parent

        // when
        xdimenResRemoverHelper.getAndRemoveXdimenRes(projectDir.toFile())

        // assert
        assertTrue(dirWithManyFiles.exists())
        assertFalse(dirWithOnlyXdimen.exists())
    }

    private fun getXdimenResFile(relativeResPath: String): Path =
        projectDir.resolve("src/main/res/$relativeResPath").apply {
            Files.createDirectories(parent)
            Files.createFile(this)
        }
}
