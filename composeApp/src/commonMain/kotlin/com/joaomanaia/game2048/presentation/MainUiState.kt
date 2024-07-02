package com.joaomanaia.game2048.presentation

import androidx.compose.ui.graphics.Color
import com.joaomanaia.game2048.core.presentation.theme.DarkThemeConfig

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Success(
        val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
        val amoledMode: Boolean = false,
        val seedColor: Color? = null
    ) : MainUiState
}
