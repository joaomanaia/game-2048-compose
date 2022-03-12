package com.infinitepower.game2048.core.datastore.manager

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun <T> getPreference(preferenceEntry: PreferenceRequest<T>): T

    fun <T> getPreferenceFlow(request: PreferenceRequest<T>): Flow<T>

    suspend fun <T> editPreference(key: Preferences.Key<T>, newValue: T)

    suspend fun clearPreferences()
}