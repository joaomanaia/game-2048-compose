package com.joaomanaia.game2048.core.manager

import androidx.datastore.preferences.core.Preferences

open class PreferenceRequest<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T
)