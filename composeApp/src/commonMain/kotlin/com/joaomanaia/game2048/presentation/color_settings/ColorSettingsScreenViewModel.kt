package com.joaomanaia.game2048.presentation.color_settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaomanaia.game2048.core.presentation.theme.WallpaperColors
import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManager
import com.joaomanaia.game2048.core.presentation.theme.DarkThemeConfig
import com.joaomanaia.game2048.domain.usecase.GetHueParamsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ColorSettingsScreenViewModel(
    private val gameDataStoreManager: DataStoreManager,
    wallpaperColors: WallpaperColors,
    getHueParamsUseCase: GetHueParamsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ColorSettingsUiState())
    val uiState = combine(
        _uiState,
        getDarkThemeConfig(),
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.AmoledMode),
        getSeedColorFlow(),
        getHueParamsUseCase()
    ) { state, darkThemeConfig, amoledMode, seedColor, hueParams ->
        state.copy(
            darkThemeConfig = darkThemeConfig,
            amoledMode = amoledMode,
            seedColor = seedColor,
            hueParams = hueParams
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ColorSettingsUiState()
    )

    init {
        wallpaperColors
            .generateWallpaperColors()
            .also { colors ->
                _uiState.update { currentState ->
                    currentState.copy(wallpaperColors = colors)
                }
            }
    }

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
            is ColorSettingsUiEvent.OnAmoledModeChanged -> updateAmoledMode(event.amoledMode)
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

    private fun updateAmoledMode(amoledMode: Boolean) {
        viewModelScope.launch {
            gameDataStoreManager.editPreference(
                key = GameDataPreferencesCommon.AmoledMode.key,
                newValue = amoledMode
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
