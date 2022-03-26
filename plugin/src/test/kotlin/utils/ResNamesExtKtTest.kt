package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.Constants.DP
import utils.Constants.SP

internal class ResNamesExtKtTest {

    @Test
    fun `test generate dimension name for positive int`() {
        // given
        val dimen = 10.0

        // when
        val result = dimen.toDimenName(DP)

        // then
        assertEquals("x10dp", result)
    }

    @Test
    fun `test generate dimension name for positive double`() {
        // given
        val dimen = 10.5

        // when
        val result = dimen.toDimenName(DP)

        // then
        assertEquals("x10_5dp", result)
    }

    @Test
    fun `test generate dimension name for negative int`() {
        // given
        val dimen = -10.0

        // when
        val result = dimen.toDimenName(DP)

        // then
        assertEquals("neg_x10dp", result)
    }

    @Test
    fun `test generate dimension name for negative double`() {
        // given
        val dimen = -10.5

        // when
        val result = dimen.toDimenName(DP)

        // then
        assertEquals("neg_x10_5dp", result)
    }

    @Test
    fun `test generate font dimension name`() {
        // given
        val dimen = -10.5

        // when
        val result = dimen.toDimenName(SP)

        // then
        assertEquals("neg_x10_5sp", result)
    }

    @Test
    fun `test generate directory res name for device width`() {
        // given
        val smallestWidth = 350

        // when
        val result = smallestWidth.toResDirName()

        // then
        assertEquals("values-w350dp", result)
    }
}
