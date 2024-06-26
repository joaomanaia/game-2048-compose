package com.joaomanaia.game2048.ui.color_settings

import com.joaomanaia.game2048.core.ui.DarkThemeConfig
import com.joaomanaia.game2048.core.ui.TileColorsGenerator

data class ColorSettingsUiState(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val hueParams: TileColorsGenerator.HueParams = TileColorsGenerator.HueParams()
)
