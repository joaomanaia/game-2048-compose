package com.infinitepower.game2048.settings.presentation.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.game2048.core.common.preferences.GameDataPreferencesCommon
import com.infinitepower.game2048.core.datastore.manager.DataStoreManager
import com.infinitepower.game2048.core.datastore.manager.settings.SettingsPreference
import com.infinitepower.game2048.domain.SaveGameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun SettingsPreferenceItem(
    item: SettingsPreference.PreferenceItem<*>,
    prefs: Preferences?,
    dataStoreManager: DataStoreManager
) {
    val scope = rememberCoroutineScope()

    when (item) {
        is SettingsPreference.PreferenceItem.ListPreference -> {
            ListPreferenceWidget(
                preference = item,
                value = prefs?.get(item.request.key) ?: item.request.defaultValue,
                onValueChange = { newValue ->
                    scope.launch(Dispatchers.IO) {
                        dataStoreManager.editPreference(item.request.key, newValue)
                    }
                }
            )
        }
    }
}