package com.joaomanaia.game2048.presentation.color_settings.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.joaomanaia.game2048.core.presentation.theme.DarkThemeConfig
import com.joaomanaia.game2048.core.presentation.theme.Game2048Theme

@Composable
@PreviewLightDark
private fun DarkThemeDialogPickerPreview() {
    Game2048Theme {
        DarkThemeDialogPicker(
            useDarkTheme = isSystemInDarkTheme(),
            amoledMode = false,
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            onDarkThemeChanged = {},
            onAmoledModeChanged = {},
            onDismissRequest = {}
        )
    }
}
