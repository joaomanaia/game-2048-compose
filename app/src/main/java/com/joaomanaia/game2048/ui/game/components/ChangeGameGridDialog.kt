package com.joaomanaia.game2048.ui.game.components

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
import com.joaomanaia.game2048.core.ui.spacing
import com.joaomanaia.game2048.R

@Composable
fun ChangeGameGridDialog(
    currentSize: String,
    onDismissRequest: () -> Unit,
    onGridSizeChange: (size: String) -> Unit
) {
    val gridSizeOptions = mapOf(
        "3" to R.string.grid_size_3,
        "4" to R.string.grid_size_4,
        "5" to R.string.grid_size_5,
        "6" to R.string.grid_size_6,
        "7" to R.string.grid_size_7,
    )

    ChangeGameGridDialogImpl(
        options = gridSizeOptions,
        currentSize = currentSize,
        onDismissRequest = onDismissRequest,
        onGridSizeChange = onGridSizeChange
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun ChangeGameGridDialogImpl(
    options: Map<String, Int>,
    currentSize: String,
    onDismissRequest: () -> Unit,
    onGridSizeChange: (size: String) -> Unit
) {
    val (selectedValue, changeSelectedValue) = remember {
        mutableStateOf(currentSize)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(id = R.string.grid_size)) },
        text = {
            LazyColumn(modifier = Modifier.selectableGroup()) {
                items(options.keys.toList()) { key ->
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
                        options[key]?.let { value ->
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
                    onGridSizeChange(selectedValue)
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}