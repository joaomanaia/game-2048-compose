package com.joaomanaia.game2048.ui

import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.core.ui.DarkThemeConfig

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Success(
        val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
        val seedColor: Color? = null
    ) : MainUiState
}
