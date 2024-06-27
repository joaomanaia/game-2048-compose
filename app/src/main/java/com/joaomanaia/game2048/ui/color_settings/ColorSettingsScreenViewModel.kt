package com.joaomanaia.game2048.ui.color_settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.manager.DataStoreManager
import com.joaomanaia.game2048.core.ui.DarkThemeConfig
import com.joaomanaia.game2048.core.ui.TileColorsGenerator
import com.joaomanaia.game2048.di.GameDataPreferences
import com.joaomanaia.game2048.domain.usecase.GetHueParamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorSettingsScreenViewModel @Inject constructor(
    @GameDataPreferences private val gameDataStoreManager: DataStoreManager,
    getHueParamsUseCase: GetHueParamsUseCase
) : ViewModel() {
    val uiState = combine(
        getDarkThemeConfig(),
        getSeedColorFlow(),
        getHueParamsUseCase()
    ) { darkThemeConfig, seedColor, hueParams ->
        ColorSettingsUiState(
            darkThemeConfig = darkThemeConfig,
            seedColor = seedColor,
            hueParams = hueParams
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ColorSettingsUiState()
    )

    private fun getDarkThemeConfig() = gameDataStoreManager
        .getPreferenceFlow(GameDataPreferencesCommon.DarkThemeConfig)
        .map(DarkThemeConfig::valueOf)

    private fun getSeedColorFlow(): Flow<Color?> = gameDataStoreManager
        .getPreferenceFlow(GameDataPreferencesCommon.SeedColor)
        .map { colorArgb ->
            if (colorArgb != -1) Color(colorArgb) else null
        }

    fun onEvent(event: ColorSettingsUiEvent) {
        when (event) {
            is ColorSettingsUiEvent.OnDarkThemeChanged -> updateDarkThemeConfig(event.config)
            is ColorSettingsUiEvent.OnSeedColorChanged -> updateSeedColor(event.color)
            is ColorSettingsUiEvent.OnIncrementHueChanged -> updateIncrementHue(event.increment)
            is ColorSettingsUiEvent.OnHueIncrementChanged -> updateHueIncrement(event.increment)
            is ColorSettingsUiEvent.OnHueSaturationChanged -> updateHueSaturation(event.saturation)
            is ColorSettingsUiEvent.OnHueLightnessChanged -> updateHueLightness(event.lightness)
        }
    }

    private fun updateDarkThemeConfig(config: DarkThemeConfig) {
        viewModelScope.launch {
            gameDataStoreManager.editPreference(
                key = GameDataPreferencesCommon.DarkThemeConfig.key,
                newValue = config.name
            )
        }
    }

    private fun updateSeedColor(color: Color) {
        viewModelScope.launch {
            gameDataStoreManager.editPreference(
                key = GameDataPreferencesCommon.SeedColor.key,
                newValue = color.toArgb()
            )
        }
    }

    private fun updateIncrementHue(increment: Boolean) {
        viewModelScope.launch {
            gameDataStoreManager.editPreference(
                key = GameDataPreferencesCommon.IncrementHue.key,
                newValue = increment
            )
        }
    }

    private fun updateHueIncrement(incrementValue: Float) {
        viewModelScope.launch {
            gameDataStoreManager.editPreference(
                key = GameDataPreferencesCommon.HueIncrementValue.key,
                newValue = incrementValue
            )
        }
    }

    private fun updateHueSaturation(saturation: Float) {
        viewModelScope.launch {
            gameDataStoreManager.editPreference(
                key = GameDataPreferencesCommon.HueSaturation.key,
                newValue = saturation
            )
        }
    }

    private fun updateHueLightness(lightness: Float) {
        viewModelScope.launch {
            gameDataStoreManager.editPreference(
                key = GameDataPreferencesCommon.HueLightness.key,
                newValue = lightness
            )
        }
    }
}
