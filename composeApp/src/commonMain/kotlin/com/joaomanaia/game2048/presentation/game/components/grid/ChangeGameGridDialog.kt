package com.joaomanaia.game2048.presentation.game.components.grid

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.joaomanaia.game2048.core.presentation.theme.spacing
import game2048.composeapp.generated.resources.Res
import game2048.composeapp.generated.resources.cancel
import game2048.composeapp.generated.resources.grid_size
import game2048.composeapp.generated.resources.grid_size_n
import game2048.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ChangeGameGridDialog(
    currentSize: Int,
    onDismissRequest: () -> Unit,
    onGridSizeChange: (size: Int) -> Unit
) {
    ChangeGameGridDialogImpl(
        sizes = listOf(3, 4, 5, 6, 7),
        currentSize = currentSize,
        onDismissRequest = onDismissRequest,
        onGridSizeChange = onGridSizeChange
    )
}

@Composable
private fun ChangeGameGridDialogImpl(
    sizes: List<Int>,
    currentSize: Int,
    onDismissRequest: () -> Unit,
    onGridSizeChange: (size: Int) -> Unit
) {
    val (selectedValue, changeSelectedValue) = remember {
        mutableStateOf(currentSize)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(Res.string.grid_size)) },
        text = {
            LazyColumn(modifier = Modifier.selectableGroup()) {
                items(items = sizes) { size ->
                    val isSelected = selectedValue == size
                    val onSelected = { changeSelectedValue(size) }
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
                        Text(
                            text = stringResource(Res.string.grid_size_n, size),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                        )
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
                Text(text = stringResource(Res.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(Res.string.cancel))
            }
        }
    )
}
