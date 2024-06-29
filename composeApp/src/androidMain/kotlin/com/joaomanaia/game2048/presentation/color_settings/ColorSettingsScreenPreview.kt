package com.joaomanaia.game2048.presentation.color_settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.joaomanaia.game2048.core.presentation.theme.Game2048Theme

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun ColorSettingsScreenPreview() {
    Game2048Theme {
        ColorSettingsScreen(
            uiState = ColorSettingsUiState()
        )
    }
}
