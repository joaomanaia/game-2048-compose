package com.infinitepower.game2048.core.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("main_screen")

    object SettingsScreen : Screen("settings_screen")
}
