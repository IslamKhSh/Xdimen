@file:Suppress("Detekt:MagicNumber", "unused")

package utils

sealed class BackgroundColor(val value: Int) {
    object Default : BackgroundColor(0)

    // normal colors
    object Black : BackgroundColor(40)
    object Red : BackgroundColor(41)
    object Green : BackgroundColor(42)
    object Yellow : BackgroundColor(43)
    object Blue : BackgroundColor(44)
    object Magenta : BackgroundColor(45)
    object Cyan : BackgroundColor(46)
    object White : BackgroundColor(47)

    // colors with high contrast
    object BlackBright : BackgroundColor(100)
    object RedBright : BackgroundColor(101)
    object GreenBright : BackgroundColor(102)
    object YellowBright : BackgroundColor(103)
    object BlueBright : BackgroundColor(104)
    object MagentaBright : BackgroundColor(105)
    object CyanBright : BackgroundColor(106)
    object WhiteBright : BackgroundColor(107)
}

sealed class TextColor(val value: Int) {
    object Default : TextColor(0)

    // normal colors
    object Black : TextColor(30)
    object Red : TextColor(31)
    object Green : TextColor(32)
    object Yellow : TextColor(33)
    object Blue : TextColor(34)
    object Magenta : TextColor(35)
    object Cyan : TextColor(36)
    object White : TextColor(37)

    // colors with high contrast
    object BlackBright : TextColor(90)
    object RedBright : TextColor(91)
    object GreenBright : TextColor(92)
    object YellowBright : TextColor(93)
    object BlueBright : TextColor(94)
    object MagentaBright : TextColor(95)
    object CyanBright : TextColor(96)
    object WhiteBright : TextColor(97)
}

/**
 * Use it to style terminal output: background color, text color and text decoration.
 *
 * @param textBackgroundColor default is normal terminal text background.
 * @param textColor default is normal terminal text color.
 * @param boldText set true to make text bold, default is false.
 * @param italicText set true to make text italic, default is false.
 * @param underLineText set true to make text underline, default is false.
 * @param action action to be performed after the terminal output customized.
 *
 * #####
 * More info:
 * - [Colors_In_Terminal](http://jafrog.com/2013/11/23/colors-in-terminal.html)
 * - [ANSI_Colors](https://gist.github.com/iamnewton/8754917)
 *
 * @author Islam Khaled
 */
fun styleOutput(
    textBackgroundColor: BackgroundColor = BackgroundColor.Default,
    textColor: TextColor = TextColor.Default,
    boldText: Boolean = false,
    italicText: Boolean = false,
    underLineText: Boolean = false,
    action: () -> Unit
) {
    // ${27.toChar()} -> `Esc` char
    val styleFormat = StringBuilder("${27.toChar()}[${textBackgroundColor.value};${textColor.value}")

    if (boldText) {
        styleFormat.append(";1")
    }

    if (italicText) {
        styleFormat.append(";3")
    }

    if (underLineText) {
        styleFormat.append(";4")
    }

    styleFormat.append("m")

    print(styleFormat) // set the terminal output to the intended style
    action() // print what you want
    print("${27.toChar()}[0m") // reset the terminal style again to default output
}
