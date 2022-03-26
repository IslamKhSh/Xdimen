package pluginExtensions

import org.gradle.api.provider.Property

abstract class DimenRange {
    abstract val minDimen: Property<Int> // min dimen to be generated
    abstract val maxDimen: Property<Int> // max dimen to be generated
    abstract val step: Property<Double> // dimen step, the step between dimens to generate
}

@Suppress("Detekt:MagicNumber")
internal fun DimenRange.initDimenRangeDefaultValues() {
    minDimen.convention(-10)
    maxDimen.convention(600)
    step.convention(1.0)
}

@Suppress("Detekt:MagicNumber")
internal fun DimenRange.initFontsRangeDefaultValues() {
    minDimen.convention(6)
    maxDimen.convention(48)
    step.convention(1.0)
}
