package com.infinitepower.game2048.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.infinitepower.game2048.core.R

@Composable
fun GameDialog(
    title: String,
    message: String,
    confirmText: String = stringResource(id = R.string.ok),
    dismissText: String = stringResource(id = R.string.cancel),
    onConfirmListener: () -> Unit,
    onDismissListener: () -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirmListener) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissListener) {
                Text(text = dismissText)
            }
        },
        onDismissRequest = onDismissListener
    )
}