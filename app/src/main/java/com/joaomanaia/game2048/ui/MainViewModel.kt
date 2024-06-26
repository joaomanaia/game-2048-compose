package com.joaomanaia.game2048.ui

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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @GameDataPreferences private val gameDataStoreManager: DataStoreManager
) : ViewModel() {
    val uiState = combine(
        getDarkThemeConfig()
    ) { darkThemeConfig ->
        darkThemeConfig
    }.map { (darkThemeConfig) ->
        MainUiState.Success(
            darkThemeConfig = darkThemeConfig
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState.Loading
    )

    private fun getDarkThemeConfig() = gameDataStoreManager
        .getPreferenceFlow(GameDataPreferencesCommon.DarkThemeConfig)
        .map(DarkThemeConfig::valueOf)
}