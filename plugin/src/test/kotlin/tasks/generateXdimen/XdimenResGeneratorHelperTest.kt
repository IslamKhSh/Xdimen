package tasks.generateXdimen

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import pluginExtensions.DimenRange
import pluginExtensions.XXHDPI
import utils.Constants.VALUES_ALTER_RES_DIR_PREFIX
import java.io.File

@ExtendWith(MockKExtension::class)
internal class XdimenResGeneratorHelperTest {

    @TempDir
    lateinit var projectDir: File

    @MockK(relaxed = true)
    private lateinit var dimenRange: DimenRange

    @MockK(relaxed = true)
    private lateinit var fontsRange: DimenRange

    private lateinit var xdimenResGeneratorHelper: XdimenResGeneratorHelper

    @BeforeEach
    fun setup() {
        xdimenResGeneratorHelper =
            XdimenResGeneratorHelper(projectRootDir = projectDir, dimensRange = dimenRange, fontDimesRange = fontsRange)

        every { dimenRange.minDimen.get() } returns -10
        every { dimenRange.maxDimen.get() } returns 600
        every { dimenRange.step.get() } returns 1.0

        every { fontsRange.minDimen.get() } returns 6
        every { fontsRange.maxDimen.get() } returns 48
        every { fontsRange.step.get() } returns 1.0
    }

    @Test
    fun `test calculate relative width`() {
        // given
        val designWidth = 480
        val designDpi = XXHDPI

        // when
        val result = xdimenResGeneratorHelper.calculateRelativeWidth(designWidth, designDpi)

        // then
        Assertions.assertEquals(160.0, result)
    }

    @Test
    fun `test create file in values dir`() {
        // given
        val defaultXdimenRes = getXdimenResFile("values/xdimen.xml")

        // when
        xdimenResGeneratorHelper.generateResources(360.0, emptySet())

        // then
        assertTrue(defaultXdimenRes.exists())
    }

    @Test
    fun `test create res for every item in targetDevicesWidth set`() {
        // given
        val targetDevicesSet = setOf(250, 300)
        val xdimenResFiles = targetDevicesSet.map {
            getXdimenResFile("${VALUES_ALTER_RES_DIR_PREFIX}${it}dp/xdimen.xml")
        }

        // when
        xdimenResGeneratorHelper.generateResources(360.0, targetDevicesSet)

        // then
        xdimenResFiles.forEach {
            assertTrue(it.exists())
        }
    }

    @Test
    fun `test generate xdimen with valid ranges`() {
        // given
        val defaultXdimenRes = getXdimenResFile("values/xdimen.xml")

        every { dimenRange.minDimen.get() } returns -10
        every { dimenRange.maxDimen.get() } returns 50
        every { dimenRange.step.get() } returns 0.5

        every { fontsRange.minDimen.get() } returns 6
        every { fontsRange.maxDimen.get() } returns 24

        // when
        xdimenResGeneratorHelper.generateResources(360.0, emptySet())

        // then
        with(defaultXdimenRes.readText()) {
            assertFalse(contains("<dimen name=\"neg_x10_5dp\">")) // -10.5dp out of range
            assertTrue(contains("<dimen name=\"neg_x10dp\">"))
            assertTrue(contains("<dimen name=\"neg_x9_5dp\">"))
            assertTrue(contains("<dimen name=\"x49_5dp\">"))
            assertTrue(contains("<dimen name=\"x50dp\">"))
            assertFalse(contains("<dimen name=\"x50_5dp\">")) // 50.5dp out of range

            assertFalse(contains("<dimen name=\"x5sp\">")) // 5sp out of range
            assertTrue(contains("<dimen name=\"x6sp\">"))
            assertTrue(contains("<dimen name=\"x24sp\">"))
            assertFalse(contains("<dimen name=\"x25sp\">")) // 25sp out of range
        }
    }

    @Test
    fun `test generate xdimen with valid scale`() {
        // given
        val relativeWidth = 250.0
        val xdimenForDeviceWidth500 = getXdimenResFile("${VALUES_ALTER_RES_DIR_PREFIX}500dp/xdimen.xml")

        every { dimenRange.minDimen.get() } returns 10
        every { dimenRange.maxDimen.get() } returns 50

        every { fontsRange.minDimen.get() } returns 6
        every { fontsRange.maxDimen.get() } returns 24

        // when
        xdimenResGeneratorHelper.generateResources(relativeWidth, setOf(500))

        // then
        with(xdimenForDeviceWidth500.readText()) {
            assertTrue(contains("<dimen name=\"x20dp\">40.00dp</dimen>"))
            assertTrue(contains("<dimen name=\"x8sp\">16.00sp</dimen>"))
        }
    }

    private fun getXdimenResFile(relativeResPath: String): File =
        projectDir.resolve("src/main/res/$relativeResPath")
}
