package com.joaomanaia.game2048.model

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import kotlin.math.abs

/**
 * Represents an HSL color with hue, saturation, and lightness values.
 *
 * @param hue The hue value in degrees (0-360).
 * @param saturation The saturation value (0-1).
 * @param lightness The lightness value (0-1).
 */
data class HslColor(
    @FloatRange(from = 0.0, to = 360.0) val hue: Float,
    @FloatRange(from = 0.0, to = 1.0) val saturation: Float,
    @FloatRange(from = 0.0, to = 1.0) val lightness: Float
) {
    init {
        require(hue in 0.0..360.0) { "Hue must be between 0 and 360 but was $hue" }
        require(saturation in 0.0..1.0) { "Saturation must be between 0 and 1 but was $saturation" }
        require(lightness in 0.0..1.0) { "Lightness must be between 0 and 1 but was $lightness" }
    }
}

internal fun Color.toHsl(): HslColor {
    val srgbColor = convert(ColorSpaces.Srgb)

    val rf = srgbColor.red
    val gf = srgbColor.green
    val bf = srgbColor.blue

    val max = maxOf(rf, gf, bf)
    val min = minOf(rf, gf, bf)
    val deltaMaxMin = max - min

    var h: Float
    val s: Float
    val l = (max + min) / 2f

    if (max == min) {
        // Monochromatic
        s = 0f
        h = s
    } else {
        h = when (max) {
            rf -> (gf - bf) / deltaMaxMin % 6f
            gf -> (bf - rf) / deltaMaxMin + 2f
            else -> (rf - gf) / deltaMaxMin + 4f
        }

        s = (deltaMaxMin / (1f - abs((2f * l - 1f).toDouble()))).toFloat()
    }

    h = (h * 60f) % 360f
    if (h < 0) {
        h += 360f
    }

    return HslColor(
        hue = h.coerceIn(0f, 360f),
        saturation = s.coerceIn(0f, 1f),
        lightness = l.coerceIn(0f, 1f)
    )
}
