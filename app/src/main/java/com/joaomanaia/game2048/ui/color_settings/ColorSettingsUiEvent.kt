package com.joaomanaia.game2048.ui.color_settings

import com.joaomanaia.game2048.core.ui.DarkThemeConfig

sealed interface ColorSettingsUiEvent {
    data class OnDarkThemeChanged(val config: DarkThemeConfig) : ColorSettingsUiEvent
}
