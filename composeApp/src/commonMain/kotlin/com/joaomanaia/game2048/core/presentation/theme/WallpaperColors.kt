package com.joaomanaia.game2048.core.presentation.theme

import androidx.compose.ui.graphics.Color

expect class WallpaperColors {
    fun isSupported(): Boolean

    fun generateWallpaperColors(): Set<Color>
}

object WallpaperColorsDefaults {
    const val DEFAULT_HUE_SHIFT = 10
    const val DEFAULT_SATURATION_SHIFT = 0.1f
    const val DEFAULT_LIGHTNESS_SHIFT = 0.1f
}
