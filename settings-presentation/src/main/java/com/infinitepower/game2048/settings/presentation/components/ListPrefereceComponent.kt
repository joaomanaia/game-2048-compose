package com.infinitepower.game2048.settings.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.infinitepower.game2048.core.datastore.manager.settings.SettingsPreference
import com.infinitepower.game2048.core.ui.spacing

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
internal fun ListPreferenceWidget(
    preference: SettingsPreference.PreferenceItem.ListPreference,
    value: String,
    onValueChange: (String) -> Unit
) {
    val (isDialogShown, showDialog) = remember { mutableStateOf(false) }

    val (selectedValue, changeSelectedValue) = remember {
        mutableStateOf(value)
    }

    TextPreferenceWidget(
        preference = preference,
        summary = value,
        onClick = {
            showDialog(true)
            changeSelectedValue(value)
            preference.onUpdateValue()
        },
    )

    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = { showDialog(false) },
            title = { Text(text = preference.title) },
            text = {
                LazyColumn(modifier = Modifier.selectableGroup()) {
                    items(preference.entries.keys.toList()) { key ->
                        val isSelected = selectedValue == key
                        val onSelected = {
                            changeSelectedValue(key)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    onClick = { if (!isSelected) onSelected() }
                                )
                                .padding(MaterialTheme.spacing.small),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isSelected) onSelected() },
                            )
                            preference.entries[key]?.let { value ->
                                Text(
                                    text = stringResource(id = value),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                                )
                            }
                        }
                    }
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            ),
            confirmButton = {
                TextButton(
                    onClick = {
                        onValueChange(selectedValue)
                        showDialog(false)
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog(false) }
                ) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
}