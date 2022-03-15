package com.infinitepower.game2048.core.datastore.manager.settings

import androidx.compose.runtime.Composable
import com.infinitepower.game2048.core.datastore.manager.PreferenceRequest

/**
 * The basic building block that represents an individual setting displayed to a user in the preference hierarchy.
 */
sealed class SettingsPreference {
    abstract val title: String
    abstract val enabled: Boolean

    /**
     * A single [SettingsPreference] item
     */
    sealed class PreferenceItem<T> : SettingsPreference() {
        abstract val summary: String
        abstract val singleLineTitle: Boolean
        abstract val icon: @Composable () -> Unit
        abstract val onUpdateValue: () -> Unit

        /**
         * 	A [PreferenceItem] that displays a list of entries as a dialog.
         * 	Only one entry can be selected at any given time.
         */
        data class ListPreference(
            val request: PreferenceRequest<String>,
            override val title: String,
            override val summary: String,
            override val singleLineTitle: Boolean,
            override val icon: @Composable () -> Unit,
            override val enabled: Boolean = true,
            override val onUpdateValue: () -> Unit,

            val entries: Map<String, Int>,
        ) : PreferenceItem<String>()
    }
}