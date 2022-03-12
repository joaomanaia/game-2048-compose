package com.infinitepower.game2048.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun GameDialog(
    title: String,
    message: String,
    onConfirmListener: () -> Unit,
    onDismissListener: () -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirmListener) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissListener) {
                Text(text = "Cancel")
            }
        },
        onDismissRequest = onDismissListener
    )
}