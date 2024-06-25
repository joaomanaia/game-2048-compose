package com.joaomanaia.game2048.ui.color_settings.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.joaomanaia.game2048.core.ui.Game2048Theme
import com.joaomanaia.game2048.core.ui.spacing

@Composable
internal fun BaseColorChooser(
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        var selectedTab by remember {
            mutableStateOf(PreviewTabButtonType.BACKGROUND_COLOR)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            PreviewTabButton(
                title = "Background color",
                selected = selectedTab == PreviewTabButtonType.BACKGROUND_COLOR,
                onClick = { selectedTab = PreviewTabButtonType.BACKGROUND_COLOR }
            )
            PreviewTabButton(
                title = "Basic color",
                selected = selectedTab == PreviewTabButtonType.BASIC_COLOR,
                onClick = { selectedTab = PreviewTabButtonType.BASIC_COLOR }
            )
        }
    }
}

private enum class PreviewTabButtonType {
    BACKGROUND_COLOR,
    BASIC_COLOR
}

@Composable
private fun RowScope.PreviewTabButton(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "Container Color"
    )

    Surface(
        modifier = modifier.weight(1f),
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.large,
        onClick = onClick,
        selected = selected,
        color = containerColor.value,
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@PreviewLightDark
private fun BaseColorChooserPreview() {
    Game2048Theme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                var color by remember {
                    mutableStateOf(Color.Yellow)
                }

                BaseColorChooser(
                    onColorSelected = { color = it }
                )
            }
        }
    }
}
