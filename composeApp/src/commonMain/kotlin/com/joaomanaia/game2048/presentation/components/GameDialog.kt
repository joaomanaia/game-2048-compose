package com.joaomanaia.game2048.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import game2048.composeapp.generated.resources.Res
import game2048.composeapp.generated.resources.cancel
import game2048.composeapp.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameDialog(
    title: String,
    message: String,
    confirmText: String = stringResource(Res.string.ok),
    dismissText: String = stringResource(Res.string.cancel),
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
