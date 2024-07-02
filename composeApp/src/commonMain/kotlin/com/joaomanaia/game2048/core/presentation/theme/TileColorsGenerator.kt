package com.joaomanaia.game2048.core.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.model.Tile
import com.joaomanaia.game2048.model.toHsl

object TileColorsGenerator {
    const val DEFAULT_INCREMENT_HUE = false
    const val DEFAULT_HUE_INCREMENT = 12f
    const val DEFAULT_SATURATION = 0.5f
    const val DEFAULT_LIGHTNESS = 0.5f

    @Immutable
    data class HueParams(
        val hueIncrement: Float = DEFAULT_HUE_INCREMENT,
        val isIncrement: Boolean = DEFAULT_INCREMENT_HUE,
        val saturation: Float = DEFAULT_SATURATION,
        val lightness: Float = DEFAULT_LIGHTNESS
    )

    fun getColorForTile(
        tile: Tile,
        baseColor: Color,
        hueParams: HueParams = HueParams()
    ): Color = getColorForTile(tile.logNum, baseColor, hueParams)

    fun getColorForTile(
        logNum: Int,
        baseColor: Color,
        hueParams: HueParams = HueParams()
    ): Color {
        // Calculate the hue increment based on the log number of the tile,
        // using the index of the tile in the sequence.
        val tileHueIncrement = (logNum - 1) * hueParams.hueIncrement
        val baseHue = baseColor.toHsl().hue

        val hue = calculateHue(baseHue, tileHueIncrement, hueParams.isIncrement)

        return Color.hsl(hue, hueParams.saturation, hueParams.lightness)
    }

    /**
     * Generates a sequence of colors by incrementing or decrementing the hue of the given base color.
     *
     * @param baseColor The color to start with.
     * @return A sequence of colors with changing hue values.
     */
    fun generateHueSequence(
        baseColor: Color,
        hueParams: HueParams = HueParams()
    ): Sequence<Color> = generateSequence(baseColor.toHsl().hue) { hue ->
        calculateHue(hue, hueParams.hueIncrement, hueParams.isIncrement)
    }.map { Color.hsl(it, hueParams.saturation, hueParams.lightness) }

    private fun calculateHue(
        hue: Float,
        hueIncrement: Float,
        isIncrement: Boolean
    ): Float {
        return if (isIncrement) {
            (hue + hueIncrement) % 360f
        } else {
            (hue - hueIncrement + 360f) % 360f
        }.coerceIn(0f, 360f)
    }
}
