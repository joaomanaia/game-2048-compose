package com.joaomanaia.game2048.core.ui

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.model.toHsl

class WallpaperColors(
    private val context: Context
) {
    companion object {
        private const val DEFAULT_HUE_SHIFT = 10
        private const val DEFAULT_SATURATION_SHIFT = 0.1f
        private const val DEFAULT_LIGHTNESS_SHIFT = 0.1f
    }

    private fun isSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
    }

    // Generates a list of colors from the wallpaper, one main primary and
    // other two variations.
    fun generateWallpaperColors(): Set<Color> {
        if (!isSupported()) {
            return emptySet()
        }

        val primaryColor = generateWallpaperPrimaryColor() ?: return emptySet()

        return generateWallpaperVariations(primaryColor)
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    private fun generateWallpaperPrimaryColor(): Color? {
        val colors = WallpaperManager
            .getInstance(context)
            .getWallpaperColors(WallpaperManager.FLAG_SYSTEM)

        val primaryColorArgb = colors?.primaryColor?.toArgb()
        return primaryColorArgb?.let { Color(it) }
    }

    private fun generateWallpaperVariations(primaryColor: Color): Set<Color> {
        val positiveShiftVariations = primaryColor.generateVariations(
            hueShift = DEFAULT_HUE_SHIFT,
            saturationShift = DEFAULT_SATURATION_SHIFT,
            lightnessShift = DEFAULT_LIGHTNESS_SHIFT
        )

        val negativeShiftVariations = primaryColor.generateVariations(
            hueShift = -DEFAULT_HUE_SHIFT,
            saturationShift = -DEFAULT_SATURATION_SHIFT,
            lightnessShift = -DEFAULT_LIGHTNESS_SHIFT
        )

        return setOf(primaryColor, positiveShiftVariations, negativeShiftVariations)
    }

    private fun Color.generateVariations(
        hueShift: Int,
        saturationShift: Float,
        lightnessShift: Float
    ): Color {
        val hsl = toHsl()

        val hue = (hsl.hue + hueShift) % 360
        val saturation = hsl.saturation + saturationShift
        val lightness = hsl.lightness + lightnessShift

        return Color.hsl(
            hue = hue.coerceIn(0f, 360f),
            saturation = saturation.coerceIn(0f, 1f),
            lightness = lightness.coerceIn(0f, 1f)
        )
    }
}
