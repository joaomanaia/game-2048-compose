package com.joaomanaia.game2048.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    DARK,
    LIGHT;

    @Composable
    fun shouldUseDarkTheme(): Boolean {
        return when (this) {
            FOLLOW_SYSTEM -> isSystemInDarkTheme()
            DARK -> true
            LIGHT -> false
        }
    }
}
