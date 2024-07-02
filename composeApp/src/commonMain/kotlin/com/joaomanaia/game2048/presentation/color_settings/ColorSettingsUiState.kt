package com.joaomanaia.game2048.presentation.color_settings

import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.core.presentation.theme.DarkThemeConfig
import com.joaomanaia.game2048.core.presentation.theme.TileColorsGenerator

data class ColorSettingsUiState(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val amoledMode: Boolean = false,
    val seedColor: Color? = null,
    val hueParams: TileColorsGenerator.HueParams = TileColorsGenerator.HueParams(),
    val wallpaperColors: Set<Color> = emptySet(),
)
