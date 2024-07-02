package com.joaomanaia.game2048.core.datastore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

actual abstract class Preferences internal actual constructor() {
    actual abstract fun asMap(): Map<PreferencesKey<*>, Any>

    actual abstract operator fun <T> contains(key: PreferencesKey<T>): Boolean

    actual abstract operator fun <T> get(key: PreferencesKey<T>): T?

    actual fun toMutablePreferences(): MutablePreferences {
        return MutablePreferences(asMap().toMutableMap(), startFrozen = false)
    }

    actual fun toPreferences(): Preferences {
        return MutablePreferences(asMap().toMutableMap(), startFrozen = true)
    }
}

actual class PreferencesKey<T> internal actual constructor(
    actual val name: String
) {
    actual infix fun to(value: T): PreferencesPair<T> = PreferencesPair(this, value)

    actual override fun equals(other: Any?): Boolean =
        if (other is PreferencesKey<*>) {
            name == other.name
        } else {
            false
        }

    actual override fun hashCode(): Int {
        return name.hashCode()
    }

    actual override fun toString(): String = name
}

actual class PreferencesPair<T> internal actual constructor(
    actual val key: PreferencesKey<T>,
    actual val value: T
)

actual class MutablePreferences internal constructor(
    internal val preferencesMap: MutableMap<PreferencesKey<*>, Any> = mutableMapOf(),
    startFrozen: Boolean = true
) : Preferences() {
    override fun asMap(): Map<PreferencesKey<*>, Any> {
        return immutableMap(
            preferencesMap.entries.associate { entry ->
                when (val value = entry.value) {
                    is ByteArray -> Pair(entry.key, value.copyOf())
                    else -> Pair(entry.key, entry.value)
                }
            }
        )
    }

    override fun <T> contains(key: PreferencesKey<T>): Boolean {
        return preferencesMap.containsKey(key)
    }

    override fun <T> get(key: PreferencesKey<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return when (val value = preferencesMap[key]) {
            is ByteArray -> value.copyOf()
            else -> value
        }
                as T?
    }

    operator fun <T> set(key: PreferencesKey<T>, value: T) {
        setUnchecked(key, value)
    }

    internal fun setUnchecked(key: PreferencesKey<*>, value: Any?) {
        when (value) {
            null -> remove(key)
            // Copy set so changes to input don't change Preferences. Wrap in unmodifiableSet so
            // returned instances can't be changed.
            is Set<*> -> preferencesMap[key] = immutableCopyOfSet(value)
            is ByteArray -> preferencesMap[key] = value.copyOf()
            else -> preferencesMap[key] = value
        }
    }

    fun <T> remove(key: PreferencesKey<T>): T {
        return preferencesMap.remove(key) as T
    }

    operator fun plusAssign(pair: PreferencesPair<*>) {
        putAll(pair)
    }

    fun putAll(vararg pairs: PreferencesPair<*>) {
        pairs.forEach { setUnchecked(it.key, it.value) }
    }

    fun clear() {
        preferencesMap.clear()
    }
}

suspend fun MutableStateFlow<Preferences>.edit(
    transform: suspend (MutablePreferences) -> Unit
): Preferences {
    return this.updateAndGet {
        // It's safe to return MutablePreferences since we freeze it in
        // PreferencesDataStore.updateData()
        it.toMutablePreferences().apply { transform(this) }
    }
}

private fun <K, V> immutableMap(map: Map<K, V>): Map<K, V> {
    // TODO:(b/239829063) Find a replacement for java's unmodifyable map.  For now just make a copy.
    return map.toMap()
}

private fun <T> immutableCopyOfSet(set: Set<T>): Set<T> = set.toSet()
