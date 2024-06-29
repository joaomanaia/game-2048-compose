package com.joaomanaia.game2048.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joaomanaia.game2048.core.navigation.Screen
import com.joaomanaia.game2048.core.presentation.theme.Game2048Theme
import com.joaomanaia.game2048.presentation.color_settings.ColorSettingsScreen
import com.joaomanaia.game2048.presentation.game.GameScreen
import org.koin.compose.KoinContext

@Composable
internal fun App(
    windowSizeClass: WindowSizeClass,
    uiState: MainUiState,
) {
    val darkTheme = shouldUseDarkTheme(uiState)

    Game2048Theme(
        seedColor = getSeedColor(uiState),
        useDarkTheme = darkTheme,
        amoledMode = getAmoledMode(uiState)
    ) {
        KoinContext {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.GAME.name
                ) {
                    composable(Screen.GAME.name) {
                        GameScreen(
                            windowSizeClass = windowSizeClass,
                            navController = navController
                        )
                    }

                    composable(Screen.COLOR_SETTINGS.name) {
                        ColorSettingsScreen(navController = navController)
                    }
                }
            }
        }
    }
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainUiState,
): Boolean = when (uiState) {
    MainUiState.Loading -> isSystemInDarkTheme()
    is MainUiState.Success -> uiState.darkThemeConfig.shouldUseDarkTheme()
}

private fun getSeedColor(uiState: MainUiState): Color? {
    return when (uiState) {
        MainUiState.Loading -> null
        is MainUiState.Success -> uiState.seedColor
    }
}

private fun getAmoledMode(uiState: MainUiState): Boolean {
    return when (uiState) {
        MainUiState.Loading -> false
        is MainUiState.Success -> uiState.amoledMode
    }
}
