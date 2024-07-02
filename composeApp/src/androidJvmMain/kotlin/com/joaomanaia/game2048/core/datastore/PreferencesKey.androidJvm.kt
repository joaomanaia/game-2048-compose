package com.joaomanaia.game2048.core.datastore

import androidx.datastore.preferences.core.Preferences as AndroidxPreferences
import androidx.datastore.preferences.core.MutablePreferences as AndroidxMutablePreferences

actual typealias Preferences = AndroidxPreferences
actual typealias MutablePreferences = AndroidxMutablePreferences

actual typealias PreferencesKey <T> = AndroidxPreferences.Key<T>
actual typealias PreferencesPair <T> = AndroidxPreferences.Pair<T>
