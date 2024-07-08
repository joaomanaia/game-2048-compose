package com.joaomanaia.game2048.core.datastore.manager

import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.datastore.MutablePreferences
import com.joaomanaia.game2048.core.datastore.Preferences
import com.joaomanaia.game2048.core.datastore.PreferencesKey
import com.joaomanaia.game2048.core.datastore.PreferencesPair
import com.joaomanaia.game2048.core.datastore.edit
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val logger = KotlinLogging.logger("DataStoreManagerImpl")

actual class DataStoreManagerImpl : DataStoreManager {
    private val mutablePreferences = MutableStateFlow<Preferences>(
        MutablePreferences(preferencesMap = loadFromLocalStorage())
    )
    override val preferenceFlow: Flow<Preferences> = mutablePreferences.asSharedFlow()

    private fun loadFromLocalStorage(): MutableMap<PreferencesKey<*>, Any> {
        val allPrefs = GameDataPreferencesCommon.allPreferences()
        val loadedPrefs = mutableMapOf<PreferencesKey<*>, Any>()

        for (i in 0 until localStorage.length) {
            val keyStr = localStorage.key(i) ?: continue
            val req = allPrefs.find { it.key.name == keyStr } ?: continue

            val value = getPreferenceValueFromLocalStorage(req)
            loadedPrefs[req.key] = value
        }

        logger.debug { "Loaded ${loadedPrefs.size} preferences from localStorage" }

        return loadedPrefs
    }

    private fun <T> getPreferenceValueFromLocalStorage(req: PreferenceRequest<T>): T {
        val valueStr = localStorage.getItem(req.key.name) ?: return req.defaultValue

        return when (req.defaultValue) {
            is String -> valueStr
            is Boolean -> valueStr.toBoolean()
            is Int -> valueStr.toInt()
            is Float -> valueStr.toFloat()
            else -> throw IllegalArgumentException("Unsupported type: ${req.defaultValue!!::class}")
        } as T
    }

    override suspend fun <T> getPreference(preferenceEntry: PreferenceRequest<T>): T {
        return preferenceFlow
            .firstOrNull()
            ?.get(preferenceEntry.key) ?: preferenceEntry.defaultValue
    }

    override fun <T> getPreferenceFlow(request: PreferenceRequest<T>): Flow<T> = preferenceFlow.map {
        it[request.key] ?: request.defaultValue
    }.distinctUntilChanged()

    override suspend fun <T> editPreference(key: PreferencesKey<T>, newValue: T) {
        saveToLocalStorage(key, newValue)
        mutablePreferences.edit { preferences -> preferences[key] = newValue }
    }

    override suspend fun editPreferences(vararg prefs: PreferencesPair<*>) {
        mutablePreferences.edit { preferences ->
            prefs.forEach { pref ->
                saveToLocalStorage(pref.key, pref.value)
                preferences.plusAssign(pref)
            }
        }
    }

    private fun saveToLocalStorage(key: PreferencesKey<*>, value: Any?) {
        when (value) {
            is String -> localStorage.setItem(key.name, value)
            is Boolean -> localStorage.setItem(key.name, value.toString())
            is Int -> localStorage.setItem(key.name, value.toString())
            is Float -> localStorage.setItem(key.name, value.toString())
            else -> throw IllegalArgumentException("Unsupported type: $value")
        }
    }

    override suspend fun clearPreferences() {
        mutablePreferences.edit { preferences -> preferences.clear() }
    }
}
