package com.joaomanaia.game2048.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.materialkolor.rememberDynamicColorScheme

@Composable
actual fun Game2048Theme(
    seedColor: Color?,
    useDarkTheme: Boolean,
    amoledMode: Boolean,
    isDynamic: Boolean, // Unused parameter
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        seedColor != null -> {
            rememberDynamicColorScheme(
                seedColor = seedColor,
                isDark = useDarkTheme,
                isAmoled = amoledMode
            )
        }
        useDarkTheme -> DarkThemeColors
        else -> LightThemeColors
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
