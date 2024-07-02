package com.joaomanaia.game2048.core.datastore

expect abstract class Preferences internal constructor() {
    abstract fun asMap(): Map<PreferencesKey<*>, Any>

    abstract operator fun <T> contains(key: PreferencesKey<T>): Boolean

    abstract operator fun <T> get(key: PreferencesKey<T>): T?

    fun toMutablePreferences(): MutablePreferences

    fun toPreferences(): Preferences
}

expect class PreferencesKey<T> internal constructor(name: String) {
    val name: String

    override operator fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    infix fun to(value: T): PreferencesPair<T>

    override fun toString(): String
}

expect class PreferencesPair<T> internal constructor(key: PreferencesKey<T>, value: T) {
    internal val key: PreferencesKey<T>

    internal val value: T
}


expect class MutablePreferences : Preferences

fun booleanPreferencesKey(name: String): PreferencesKey<Boolean> = PreferencesKey(name)

fun intPreferencesKey(name: String): PreferencesKey<Int> = PreferencesKey(name)

fun floatPreferencesKey(name: String): PreferencesKey<Float> = PreferencesKey(name)

fun stringPreferencesKey(name: String): PreferencesKey<String> = PreferencesKey(name)

