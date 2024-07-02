package com.joaomanaia.game2048.core.presentation.theme

import androidx.compose.ui.graphics.Color

actual class WallpaperColors {
    actual fun isSupported(): Boolean = false

    actual fun generateWallpaperColors(): Set<Color> = emptySet()
}
