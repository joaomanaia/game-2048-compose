package com.joaomanaia.game2048.presentation.color_settings.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.joaomanaia.game2048.core.presentation.theme.spacing

private val defaultBasicColors = setOf(Color.Blue, Color.Red, Color.Green, Color.Yellow)

@Composable
internal fun BaseColorChooser(
    modifier: Modifier = Modifier,
    currentSeedColor: Color?,
    useDarkTheme: Boolean,
    wallpaperColors: Set<Color>,
    defaultSelectedTab: PreviewTabButtonType = PreviewTabButtonType.BACKGROUND_COLOR,
    onColorSelected: (Color) -> Unit
) {
    var selectedTab by remember { mutableStateOf(defaultSelectedTab) }

    val colorsToSelect: Set<Color> = remember(selectedTab, wallpaperColors) {
        if (selectedTab == PreviewTabButtonType.BACKGROUND_COLOR && wallpaperColors.isNotEmpty()) {
           wallpaperColors
        } else {
            defaultBasicColors
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        if (wallpaperColors.isNotEmpty()) {
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

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            items(items = colorsToSelect.toList()) { color ->
                SelectablePaletteItem(
                    modifier = Modifier.size(75.dp),
                    baseColor = color,
                    useDarkTheme = useDarkTheme,
                    selected = currentSeedColor == color,
                    onClick = { onColorSelected(color) }
                )
            }
        }
    }
}

internal enum class PreviewTabButtonType {
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
