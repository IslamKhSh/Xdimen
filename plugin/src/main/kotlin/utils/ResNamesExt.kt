package utils

import utils.Constants.DP
import utils.Constants.VALUES_ALTER_RES_DIR_PREFIX

/**
 * As in xml attributes values names restricted to only **chars**, **numbers** and **`_`** symbol
 * and must start with a char, so we map numbers as follows:
 *  - negative numbers ==> add **neg`_`** and remove **-** sign.
 *  - add **x** prefix before numeric value.
 *  - remove extra zeros after dot in double number (10.500 => 10.5)
 *  - remove **.** (dot sign) if it's the last char (this means all digits after dot were zeros).
 *  - replace **.** (dot sign) with **`_`** symbol if it's not removed.
 *  - add the dimen [unit] as a suffix.
 *
 *   for example:
 *   - **10.00** ==> **x10.** ==> **x10** ==> **x10dp**
 *   - **10.50** ==> **x10.5** ==> **x10_5** ==> **x10_5dp**
 *   - **-10.00** ==> **neg_10.00** ==> **neg_x10.** ==> **neg_x10** ==> **neg_x10dp**
 *   - **-10.50** ==> **neg_10.50** ==> **neg_x10.50** ==> **neg_x10_5** ==> **neg_x10_5dp**
 */
internal fun Double.toDimenName(unit: String): String {
    var numAsString = "$this"
    val dimenNameBuilder = StringBuilder()

    // if number is negative append prefix neg_ and remove - sign
    if (this < 0) {
        dimenNameBuilder.append("neg_")
        numAsString = numAsString.removePrefix("-")
    }

    // add x as prefix
    // remove extra zeros after . in double number
    // remove . if it was the last char (this means all digits after dot were zeros)
    // replace . with _ if it's not removed
    dimenNameBuilder.append("x$numAsString".trimEnd { it == '0' }.trimEnd { it == '.' }.replace(".", "_"))

    // add the dimen unit (dp or sp) at the end
    dimenNameBuilder.append(unit)

    return dimenNameBuilder.toString()
}

/**
 * get alternative res dir name from the screen width.
 */
internal fun Int.toResDirName() = "$VALUES_ALTER_RES_DIR_PREFIX${this}$DP"
