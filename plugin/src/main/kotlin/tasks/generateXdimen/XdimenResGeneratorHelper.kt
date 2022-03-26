package tasks.generateXdimen

import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.PrintOptions
import org.redundent.kotlin.xml.xml
import pluginExtensions.DPI
import pluginExtensions.DimenRange
import utils.Constants
import utils.Constants.RESOURCES_PATH
import utils.Constants.VALUES_RES_DIR
import utils.Constants.XDIMEN_RES_FILE_NAME
import utils.step
import utils.toDimenName
import utils.toResDirName
import java.io.File
import java.util.*

internal class XdimenResGeneratorHelper constructor(
    private val projectRootDir: File,
    private val dimensRange: DimenRange,
    private val fontDimesRange: DimenRange
) {

    /**
     * Given the design width and its density (dpi) we can calculate the equivalent width in **mdpi** density.
     *
     * **mdpi** is the baseline, so we need to calculate the scaling factor related to it.
     */
    @Suppress("Detekt:MagicNumber")
    fun calculateRelativeWidth(designWidth: Int, designDpi: DPI) = (designWidth * 160) / designDpi.value.toDouble()

    /**
     * generate xdimen resources for every target device width and a default one in `values` res directory
     * to avoid warning of this res is not in the default configuration.
     *
     * @param relativeDesignWidth the relative design width that's equivalent to the design width in case of mdpi.
     * @param targetDevicesWidth set of devices width that's intended to create res for them.
     */
    fun generateResources(
        relativeDesignWidth: Double,
        targetDevicesWidth: Set<Int>
    ) {
        // create default res file to add it in values res dir to avoid error that res not in the default configuration
        createXdimenResource(valuesResDirName = VALUES_RES_DIR, dimenFactor = 1.0)

        // create res file for every device from targeted
        targetDevicesWidth.forEach {
            createXdimenResource(valuesResDirName = it.toResDirName(), dimenFactor = it / relativeDesignWidth)
        }
    }

    /**
     * create xdimen smallest-width alternative res file for target device width with scaled dimensions
     * according to [dimenFactor].
     *
     * @param valuesResDirName the name of alternative res dir, generated from device width : 240 -> values-sw240dp.
     * @param dimenFactor the scaling factor
     *
     * @return the created xdimen res file.
     */
    private fun createXdimenResource(
        valuesResDirName: String,
        dimenFactor: Double
    ) {
        val resourcesDir = File(projectRootDir, "$RESOURCES_PATH/$valuesResDirName")
        resourcesDir.mkdirs()
        val xdimenFile = File(resourcesDir, XDIMEN_RES_FILE_NAME)

        // generate res file content with root tag resources
        val xdimenContent = xml("resources") {
            generateDimensContent(dimenFactor, false) // create dimens
            generateDimensContent(dimenFactor, true) // create fonts
        }

        // write content to generated file
        xdimenFile.bufferedWriter().use {
            it.write(xdimenContent.toString(PrintOptions(singleLineTextElements = true)))
        }

        println("Xdimen resource created: ${xdimenFile.relativeTo(projectRootDir).invariantSeparatorsPath}")
    }

    /**
     * Generate the scaled dimensions xml content from the given range.
     *
     * @param dimenFactor the factor to use in scaling.
     * @param isFontDimen whether this range normal dimensions or font dimensions.
     */
    private fun Node.generateDimensContent(dimenFactor: Double, isFontDimen: Boolean) {
        comment(if (isFontDimen) "Fonts" else "Dimens")
        val unit = if (isFontDimen) Constants.SP else Constants.DP

        val range = if (isFontDimen) fontDimesRange else dimensRange

        for (dimen in range.minDimen.get().toDouble()..range.maxDimen.get().toDouble() step range.step.get()) {
            // no need to scale it.
            if (dimen == 0.0) continue

            "dimen" {
                attribute("name", dimen.toDimenName(unit)) // attribute e.g: name=x1dp
                -"${String.format(Locale.ENGLISH, "%.2f", dimen * dimenFactor)}$unit"
            }
        }
    }
}
