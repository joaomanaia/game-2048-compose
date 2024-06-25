package com.joaomanaia.game2048.ui.color_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.manager.DataStoreManager
import com.joaomanaia.game2048.core.ui.DarkThemeConfig
import com.joaomanaia.game2048.di.GameDataPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorSettingsScreenViewModel @Inject constructor(
    @GameDataPreferences private val gameDataStoreManager: DataStoreManager
) : ViewModel() {
    val uiState = combine(
        getDarkThemeConfig()
    ) { darkThemeConfig ->
        darkThemeConfig
    }.map { (darkThemeConfig) ->
        ColorSettingsUiState(
            darkThemeConfig = darkThemeConfig
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ColorSettingsUiState()
    )

    private fun getDarkThemeConfig() = gameDataStoreManager
        .getPreferenceFlow(GameDataPreferencesCommon.DarkThemeConfig)
        .map(DarkThemeConfig::valueOf)

    fun onEvent(event: ColorSettingsUiEvent) {
        when (event) {
            is ColorSettingsUiEvent.OnDarkThemeChanged -> updateDarkThemeConfig(event.config)
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
}
