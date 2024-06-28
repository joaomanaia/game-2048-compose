package com.joaomanaia.game2048.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.manager.DataStoreManager
import com.joaomanaia.game2048.core.ui.DarkThemeConfig
import com.joaomanaia.game2048.di.GameDataPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @GameDataPreferences private val gameDataStoreManager: DataStoreManager
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
