package com.infinitepower.game2048.settings.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitepower.game2048.core.datastore.manager.settings.LocalPreferenceEnabledStatus
import com.infinitepower.game2048.core.datastore.manager.settings.SettingsPreference
import com.infinitepower.game2048.core.util.ui.StatusWrapper

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun TextPreferenceWidget(
    preference: SettingsPreference.PreferenceItem<*>,
    summary: String? = null,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled
    StatusWrapper(enabled = isEnabled) {
        ListItem(
            text = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            secondaryText = {
                Text(
                    text = summary ?: preference.summary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            icon = preference.icon,
            modifier = Modifier.clickable(onClick = { if (isEnabled) onClick() }),
            trailing = trailing,
        )
    }
}


@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun TextPreferenceWidgetRes(
    preference: SettingsPreference.PreferenceItem<*>,
    @StringRes summary: Int? = null,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled
    StatusWrapper(enabled = isEnabled) {
        ListItem(
            text = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            secondaryText = {
                Text(
                    text = if (summary != null) stringResource(id = summary) else preference.summary,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            icon = preference.icon,
            modifier = Modifier.clickable(onClick = { if (isEnabled) onClick() }),
            trailing = trailing,
        )
    }
}