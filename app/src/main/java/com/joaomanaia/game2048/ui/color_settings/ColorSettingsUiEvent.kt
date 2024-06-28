package com.joaomanaia.game2048.ui.color_settings

import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.core.ui.DarkThemeConfig

sealed interface ColorSettingsUiEvent {
    data class OnDarkThemeChanged(val config: DarkThemeConfig) : ColorSettingsUiEvent

    data class OnAmoledModeChanged(val amoledMode: Boolean) : ColorSettingsUiEvent

    data class OnSeedColorChanged(val color: Color) : ColorSettingsUiEvent

    data class OnIncrementHueChanged(val increment: Boolean) : ColorSettingsUiEvent

    data class OnHueIncrementChanged(val increment: Float) : ColorSettingsUiEvent

    data class OnHueSaturationChanged(val saturation: Float) : ColorSettingsUiEvent

    data class OnHueLightnessChanged(val lightness: Float) : ColorSettingsUiEvent
}
