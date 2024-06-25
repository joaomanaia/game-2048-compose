package com.joaomanaia.game2048.ui.color_settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.window.Dialog
import com.joaomanaia.game2048.core.ui.DarkThemeConfig
import com.joaomanaia.game2048.core.ui.spacing

@Composable
internal fun DarkThemeDialogPicker(
    darkThemeConfig: DarkThemeConfig,
    onDarkThemeChanged: (DarkThemeConfig) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = "Dark theme",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )
                DarkThemeConfig.entries.forEach { item ->
                    ThemeRadioButton(
                        darkThemeConfig = item,
                        selected = item == darkThemeConfig,
                        onClick = {
                            onDarkThemeChanged(item)
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeRadioButton(
    modifier: Modifier = Modifier,
    darkThemeConfig: DarkThemeConfig,
    selected: Boolean,
    onClick: () -> Unit
) {
    ThemeRadioButton(
        modifier = modifier,
        title = darkThemeConfig.getRadioButtonTitle(),
        selected = selected,
        onClick = onClick
    )
}

@Composable
private fun ThemeRadioButton(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(
                onClick = onClick,
                role = Role.RadioButton
            ),
        headlineContent = { Text(text = title) },
        trailingContent = {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    )
}

private fun DarkThemeConfig.getRadioButtonTitle(): String {
    return when (this) {
        DarkThemeConfig.FOLLOW_SYSTEM -> "Follow System"
        DarkThemeConfig.LIGHT -> "Disabled"
        DarkThemeConfig.DARK -> "Enabled"
    }
}
