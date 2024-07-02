package com.joaomanaia.game2048.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManager
import com.joaomanaia.game2048.core.presentation.theme.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val gameDataStoreManager: DataStoreManager
) : ViewModel() {
    val uiState = combine(
        getDarkThemeConfigFlow(),
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.AmoledMode),
        getSeedColorFlow()
    ) { darkThemeConfig, amoledMode, seedColor ->
        MainUiState.Success(
            darkThemeConfig = darkThemeConfig,
            amoledMode = amoledMode,
            seedColor = seedColor
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState.Loading
    )

    private fun getDarkThemeConfigFlow() = gameDataStoreManager
        .getPreferenceFlow(GameDataPreferencesCommon.DarkThemeConfig)
        .map(DarkThemeConfig::valueOf)

    private fun getSeedColorFlow(): Flow<Color?> = gameDataStoreManager
        .getPreferenceFlow(GameDataPreferencesCommon.SeedColor)
        .map { colorArgb ->
            if (colorArgb != -1) Color(colorArgb) else null
        }
}
