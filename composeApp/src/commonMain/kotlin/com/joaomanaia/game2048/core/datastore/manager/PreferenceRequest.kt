package com.joaomanaia.game2048.core.datastore.manager

import com.joaomanaia.game2048.core.datastore.PreferencesKey

open class PreferenceRequest<T>(
    val key: PreferencesKey<T>,
    val defaultValue: T
)
