package com.joaomanaia.game2048.core.common.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.joaomanaia.game2048.core.manager.PreferenceRequest
import com.joaomanaia.game2048.core.ui.DarkThemeConfig as EnumDarkThemeConfig

val Context.gameDataDataStore: DataStore<Preferences> by preferencesDataStore(name = "game_data")

object GameDataPreferencesCommon {
    object Grid : PreferenceRequest<String>(stringPreferencesKey("grid"), "[]")

    object CurrentScore : PreferenceRequest<Int>(intPreferencesKey("current_score"), 0)

    object BestScore : PreferenceRequest<Int>(intPreferencesKey("best_score"), 0)

    object GridSize : PreferenceRequest<Int>(intPreferencesKey("grid_size"), 4)

    // Theme
    object DarkThemeConfig : PreferenceRequest<String>(
        key = stringPreferencesKey("dark_theme_config"),
        defaultValue = EnumDarkThemeConfig.FOLLOW_SYSTEM.name
    )
}
