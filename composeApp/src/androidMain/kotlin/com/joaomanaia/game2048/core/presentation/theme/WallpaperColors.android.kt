package com.joaomanaia.game2048.core.presentation.theme

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.core.presentation.theme.WallpaperColorsDefaults.DEFAULT_HUE_SHIFT
import com.joaomanaia.game2048.core.presentation.theme.WallpaperColorsDefaults.DEFAULT_LIGHTNESS_SHIFT
import com.joaomanaia.game2048.core.presentation.theme.WallpaperColorsDefaults.DEFAULT_SATURATION_SHIFT
import com.joaomanaia.game2048.model.toHsl

actual class WallpaperColors(
    private val context: Context
) {
    actual fun isSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
    }

    actual fun generateWallpaperColors(): Set<Color> {
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