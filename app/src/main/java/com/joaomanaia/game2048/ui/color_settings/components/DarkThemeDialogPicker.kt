package com.joaomanaia.game2048.ui.color_settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.joaomanaia.game2048.core.ui.DarkThemeConfig
import com.joaomanaia.game2048.core.ui.Game2048Theme
import com.joaomanaia.game2048.core.ui.spacing

@Composable
internal fun DarkThemeDialogPicker(
    useDarkTheme: Boolean,
    amoledMode: Boolean,
    darkThemeConfig: DarkThemeConfig,
    onDarkThemeChanged: (DarkThemeConfig) -> Unit,
    onAmoledModeChanged: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge
        ) {
            val spaceMedium = MaterialTheme.spacing.medium

            Column(
                modifier = Modifier.padding(spaceMedium)
            ) {
                Text(
                    text = "Dark theme",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(spaceMedium)
                )
                DarkThemeConfig.entries.forEach { item ->
                    ThemeRadioButton(
                        darkThemeConfig = item,
                        selected = item == darkThemeConfig,
                        onClick = { onDarkThemeChanged(item) }
                    )
                }

                AnimatedVisibility(visible = useDarkTheme) {
                    Column {
                        Text(
                            text = "Other",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = spaceMedium, top = spaceMedium),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Surface(
                            modifier = Modifier.padding(top = spaceMedium),
                            shape = MaterialTheme.shapes.large,
                            onClick = { onAmoledModeChanged(!amoledMode) }
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(text = "AMOLED Mode")
                                },
                                trailingContent = {
                                    Switch(
                                        checked = amoledMode,
                                        onCheckedChange = onAmoledModeChanged
                                    )
                                }
                            )
                        }
                    }
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

@Composable
@PreviewLightDark
private fun DarkThemeDialogPickerPreview() {
    Game2048Theme {
        DarkThemeDialogPicker(
            useDarkTheme = isSystemInDarkTheme(),
            amoledMode = false,
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            onDarkThemeChanged = {},
            onAmoledModeChanged = {},
            onDismissRequest = {}
        )
    }
}