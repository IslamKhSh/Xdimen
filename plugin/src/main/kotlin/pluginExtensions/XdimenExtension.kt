package pluginExtensions

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Nested

abstract class XdimenExtension {

    abstract val deleteOldXdimen: Property<Boolean> // delete old files with name xdimen before create new

    abstract val designWidth: Property<Int> // screen width of your ui design
    abstract val designDpi: Property<DPI> // screen density of your ui design

    @get:Nested
    abstract val dimesRange: DimenRange

    @get:Nested
    abstract val fontDimesRange: DimenRange

    // common devices width, generate alternative res for every one
    abstract val targetDevicesWidth: SetProperty<Int>

    fun dimensRange(action: Action<DimenRange>) {
        action.execute(dimesRange)
    }

    fun fontsRange(action: Action<DimenRange>) {
        action.execute(fontDimesRange)
    }

    fun targetDevicesWidth(vararg screenSizes: Int) {
        targetDevicesWidth.set(screenSizes.toSet())
    }

    // common devices with device type and orientation
    val phonePortrait: Set<Int> = phonePortraitList
    val phoneLandscape: Set<Int> = phoneLandscapeList
    val tabletPortrait: Set<Int> = tabletPortraitList
    val tabletLandscape: Set<Int> = tabletLandscapeList

    // common devices with device type
    val phoneDevices: Set<Int> = phonePortrait + phoneLandscape
    val tabletDevices = tabletPortrait + tabletLandscape
    val allDevices = phoneDevices + tabletDevices

    // common devices with orientation
    val devicesInPortrait = phonePortrait + tabletPortrait
    val devicesInLandscape = phoneLandscape + tabletLandscape

    fun ldpi() = LDPI
    fun mdpi() = MDPI
    fun hdpi() = HDPI
    fun xhdpi() = XHDPI
    fun xxhdpi() = XXHDPI
    fun xxxhdpi() = XXXHDPI
    fun tvdpi() = TVDPI
    fun dpi(value: Int) = OtherDpi(value)
}

fun Project.xdimen(block: XdimenExtension.() -> Unit) {
    extensions.configure(XdimenExtension::class.java, block)
}

/*
    These lists are the common android devices' width which collected from many sources:
        - https://en.wikipedia.org/wiki/Comparison_of_high-definition_smartphone_displays
        - https://screensiz.es/
        - https://pixensity.com/list/
 */

@Suppress("Detekt:MagicNumber")
private val phonePortraitList = setOf(
    289, 300, 325, 336, 350, 360, 369, 375, 384, 389, 393, 400, 405, 411, 415, 420, 425, 429, 432, 436, 442, 444, 450,
    458, 467, 476, 480, 492, 499, 503
)

@Suppress("Detekt:MagicNumber")
private val phoneLandscapeList = setOf(
    483, 507, 544, 549, 584, 589, 597, 628, 634, 640, 648, 656, 667, 687, 696, 709, 715, 724, 739, 743, 758, 765, 778,
    786, 790, 795, 803, 812, 820, 825, 830, 835, 844, 856, 862, 872, 880, 884, 891, 895, 900, 904, 909, 915, 921, 924,
    929, 938, 945, 950, 955, 960, 967, 972, 980, 987, 994, 1001, 1010, 1031, 1037
)

@Suppress("Detekt:MagicNumber")
private val tabletPortraitList = setOf(
    592, 625, 646, 649, 676, 684, 695, 703, 711, 721, 753, 768, 787, 796, 800, 830, 850, 853, 856, 859, 888, 909, 930,
    938, 1032, 1042, 1062, 1086
)

@Suppress("Detekt:MagicNumber")
private val tabletLandscapeList = setOf(
    833, 948, 1005, 1024, 1039, 1077, 1081, 1095, 1113, 1125, 1133, 1137, 1154, 1195, 1209, 1241, 1256, 1280, 1329,
    1334, 1365, 1396, 1374, 1401, 1422, 1594, 1616, 1630, 1650, 1853
)
