package utils

/**
 * there's no `DoubleProgression` in kotlin as [IntProgression] so we can't use `step` in for loop with double ranges.
 * this is a workaround to allow using `step` with double ranges.
 */
internal infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}
