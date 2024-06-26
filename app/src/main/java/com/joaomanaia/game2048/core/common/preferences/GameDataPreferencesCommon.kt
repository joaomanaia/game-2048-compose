package com.joaomanaia.game2048.core.common.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.joaomanaia.game2048.core.manager.PreferenceRequest
import com.joaomanaia.game2048.core.ui.TileColorsGenerator
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

    object IncrementHue : PreferenceRequest<Boolean>(
        key = booleanPreferencesKey("increment_hue"),
        defaultValue = TileColorsGenerator.DEFAULT_INCREMENT_HUE
    )

    object HueIncrementValue : PreferenceRequest<Float>(
        key = floatPreferencesKey("hue_increment_value"),
        defaultValue = TileColorsGenerator.DEFAULT_HUE_INCREMENT
    )

    object HueSaturation : PreferenceRequest<Float>(
        key = floatPreferencesKey("hue_saturation"),
        defaultValue = TileColorsGenerator.DEFAULT_SATURATION
    )

    object HueLightness : PreferenceRequest<Float>(
        key = floatPreferencesKey("hue_lightness"),
        defaultValue = TileColorsGenerator.DEFAULT_LIGHTNESS
    )
}
