package com.infinitepower.game2048.core.common.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.game2048.core.datastore.manager.PreferenceRequest
import com.infinitepower.game2048.core.di.GAME_DATA_PREFERENCES_NAME

val Context.gameDataDataStore: DataStore<Preferences> by preferencesDataStore(name = GAME_DATA_PREFERENCES_NAME)

object GameDataPreferencesCommon {
    object Grid : PreferenceRequest<String>(stringPreferencesKey("grid"), "[]")

    object CurrentScore : PreferenceRequest<Int>(intPreferencesKey("current_score"), 0)

    object BestScore : PreferenceRequest<Int>(intPreferencesKey("best_score"), 0)
}