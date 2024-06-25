package com.joaomanaia.game2048.core.ui

import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.model.Tile
import com.joaomanaia.game2048.model.toHsl

object TileColorsGenerator {
    const val HUE_INCREMENT = 15f

    private const val SATURATION = 0.5f
    private const val LIGHTNESS = 0.5f

    fun getColorForTile(
        tile: Tile,
        baseColor: Color,
        hueIncrement: Float = HUE_INCREMENT,
        isIncrement: Boolean = false
    ): Color {
        // Calculate the hue increment based on the log number of the tile.
        val tileHueIncrement = (tile.logNum - 1) * hueIncrement
        val baseHue = baseColor.toHsl().hue

        val hue = calculateHue(baseHue, tileHueIncrement, isIncrement)

        return Color.hsl(hue, SATURATION, LIGHTNESS)
    }

    /**
     * Generates a sequence of colors by incrementing or decrementing the hue of the given base color.
     *
     * @param baseColor The color to start with.
     * @param hueIncrement The amount to change the hue by for each subsequent color.
     * @param isIncrement If true, the hue will be incremented; otherwise, it will be decremented.
     * @return A sequence of colors with changing hue values.
     */
    fun generateHueSequence(
        baseColor: Color,
        hueIncrement: Float = HUE_INCREMENT,
        isIncrement: Boolean = false
    ): Sequence<Color> = generateSequence(baseColor.toHsl().hue) { hue ->
        calculateHue(hue, hueIncrement, isIncrement)
    }.map { Color.hsl(it, SATURATION, LIGHTNESS) }

    private fun calculateHue(
        hue: Float,
        hueIncrement: Float,
        isIncrement: Boolean
    ): Float {
        return if (isIncrement) {
            (hue + hueIncrement) % 360f
        } else {
            (hue - hueIncrement + 360f) % 360f
        }
    }
}
