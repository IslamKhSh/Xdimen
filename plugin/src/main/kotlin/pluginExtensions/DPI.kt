@file:Suppress("Detekt:MagicNumber")

package pluginExtensions

import javax.inject.Inject

abstract class DPI @Inject constructor(val value: Int)

object LDPI : DPI(120)
object MDPI : DPI(160)
object HDPI : DPI(240)
object XHDPI : DPI(320)
object XXHDPI : DPI(480)
object XXXHDPI : DPI(640)
object TVDPI : DPI(213)
class OtherDpi(value: Int) : DPI(value)
