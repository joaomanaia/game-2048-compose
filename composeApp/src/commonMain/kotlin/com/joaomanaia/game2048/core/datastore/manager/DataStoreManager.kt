package com.joaomanaia.game2048.core.datastore.manager

import com.joaomanaia.game2048.core.datastore.Preferences
import com.joaomanaia.game2048.core.datastore.PreferencesKey
import com.joaomanaia.game2048.core.datastore.PreferencesPair
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    val preferenceFlow: Flow<Preferences>

    suspend fun <T> getPreference(preferenceEntry: PreferenceRequest<T>): T

    fun <T> getPreferenceFlow(request: PreferenceRequest<T>): Flow<T>

    suspend fun <T> editPreference(key: PreferencesKey<T>, newValue: T)

    suspend fun editPreferences(vararg prefs: PreferencesPair<*>)

    suspend fun clearPreferences()
}
