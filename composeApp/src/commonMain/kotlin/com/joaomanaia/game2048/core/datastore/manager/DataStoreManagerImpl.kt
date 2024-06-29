package com.joaomanaia.game2048.core.datastore.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreManager {
    override val preferenceFlow = dataStore.data

    override suspend fun <T> getPreference(
        preferenceEntry: PreferenceRequest<T>
    ) = preferenceFlow
        .firstOrNull()
        ?.get(preferenceEntry.key) ?: preferenceEntry.defaultValue

    override fun <T> getPreferenceFlow(request: PreferenceRequest<T>) = preferenceFlow.map {
        it[request.key] ?: request.defaultValue
    }.distinctUntilChanged()

    override suspend fun <T> editPreference(key: Preferences.Key<T>, newValue: T) {
        dataStore.edit { preferences -> preferences[key] = newValue }
    }

    override suspend fun editPreferences(vararg prefs: Preferences.Pair<*>) {
        dataStore.edit { preferences ->
            prefs.forEach {
                preferences.plusAssign(it)
            }
        }
    }

    override suspend fun clearPreferences() {
        dataStore.edit { preferences -> preferences.clear() }
    }
}
