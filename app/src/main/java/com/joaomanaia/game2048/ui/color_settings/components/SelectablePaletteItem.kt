package com.joaomanaia.game2048.ui.color_settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.joaomanaia.game2048.core.compose.preview.BooleanPreviewProvider
import com.joaomanaia.game2048.core.ui.Game2048Theme
import com.joaomanaia.game2048.core.ui.spacing
import com.materialkolor.rememberDynamicColorScheme

@Composable
internal fun SelectablePaletteItem(
    modifier: Modifier = Modifier,
    baseColor: Color,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val colorScheme = rememberDynamicColorScheme(
        seedColor = baseColor,
        isDark = useDarkTheme
    )

    SelectablePaletteItem(
        modifier = modifier,
        colorScheme = colorScheme,
        selected = selected,
        onClick = onClick
    )
}

@Composable
private fun SelectablePaletteItem(
    modifier: Modifier = Modifier,
    colorScheme: ColorScheme,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .drawPalette(colorPrimary = colorScheme.primary)
        ) {
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Selected",
                    tint = colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .background(
                            color = colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                        .padding(SELECTED_CIRCLE_ICON_PADDING)
                )
            }
        }
    }
}

/**
 * Draw a palette with 1 circle, representing the primary color.
 */
private fun Modifier.drawPalette(
    colorPrimary: Color,
): Modifier = drawBehind {
    // Draw the top half circle
    drawArc(
        color = colorPrimary,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
    )
}

/**
 * Draw a palette with 3 circles, representing the primary, secondary, and tertiary colors.
 */
private fun Modifier.drawPalette(
    colorPrimary: Color,
    colorSecondary: Color,
    colorTertiary: Color,
): Modifier = drawBehind {
    // Draw the top half circle
    drawArc(
        color = colorPrimary,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
    )

    // Draw the bottom left quarter circle
    drawArc(
        color = colorSecondary,
        startAngle = 90f,
        sweepAngle = 90f,
        useCenter = true,
    )

    // Draw the bottom right quarter circle
    drawArc(
        color = colorTertiary,
        startAngle = 0f,
        sweepAngle = 90f,
        useCenter = true,
    )
}

private val SELECTED_CIRCLE_ICON_PADDING = 6.dp

@Composable
@PreviewLightDark
private fun PaletteItemPreview(
    @PreviewParameter(BooleanPreviewProvider::class) selected: Boolean
) {
    Game2048Theme {
        Surface {
            SelectablePaletteItem(
                modifier = Modifier
                    .padding(8.dp)
                    .size(75.dp),
                selected = selected,
                baseColor = Color.Blue,
                onClick = {}
            )
        }
    }
}
