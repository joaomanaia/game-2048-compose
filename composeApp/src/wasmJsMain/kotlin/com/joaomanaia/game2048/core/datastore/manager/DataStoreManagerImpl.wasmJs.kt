package com.joaomanaia.game2048.core.datastore.manager

import com.joaomanaia.game2048.core.datastore.MutablePreferences
import com.joaomanaia.game2048.core.datastore.Preferences
import com.joaomanaia.game2048.core.datastore.PreferencesKey
import com.joaomanaia.game2048.core.datastore.PreferencesPair
import com.joaomanaia.game2048.core.datastore.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

actual class DataStoreManagerImpl : DataStoreManager {
    private val mutablePreferences = MutableStateFlow<Preferences>(MutablePreferences())
    override val preferenceFlow: Flow<Preferences> = mutablePreferences.asSharedFlow()

    override suspend fun <T> getPreference(preferenceEntry: PreferenceRequest<T>): T {
        return preferenceFlow
            .firstOrNull()
            ?.get(preferenceEntry.key) ?: preferenceEntry.defaultValue
    }

    override fun <T> getPreferenceFlow(request: PreferenceRequest<T>): Flow<T> = preferenceFlow.map {
        it[request.key] ?: request.defaultValue
    }.distinctUntilChanged()

    override suspend fun <T> editPreference(key: PreferencesKey<T>, newValue: T) {
        mutablePreferences.edit { preferences -> preferences[key] = newValue }
    }

    override suspend fun editPreferences(vararg prefs: PreferencesPair<*>) {
        mutablePreferences.edit { preferences ->
            prefs.forEach {
                preferences.plusAssign(it)
            }
        }
    }

    override suspend fun clearPreferences() {
        mutablePreferences.edit { preferences -> preferences.clear() }
    }
}
