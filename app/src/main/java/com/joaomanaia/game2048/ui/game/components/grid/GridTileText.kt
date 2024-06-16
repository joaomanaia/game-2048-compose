package com.joaomanaia.game2048.ui.game.components.grid

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.joaomanaia.game2048.core.ui.CustomColor
import com.joaomanaia.game2048.core.ui.ExtendedColors
import com.joaomanaia.game2048.core.ui.Game2048Theme
import com.joaomanaia.game2048.core.ui.LocalExtendedColors
import kotlinx.coroutines.launch

@Composable
internal fun GridTileText(
    modifier: Modifier = Modifier,
    num: Int,
    size: Dp,
    fromScale: Float,
    fromOffset: Offset,
    toOffset: Offset,
    moveCount: Int,
    gridSize: Int,
    isPortrait: Boolean
) {
    val animatedScale = remember { Animatable(fromScale) }
    val animatedOffset = remember { Animatable(fromOffset, Offset.VectorConverter) }

    LaunchedEffect(key1 = moveCount) {
        launch {
            animatedScale.snapTo(if (moveCount == 0) 1f else fromScale)
            animatedScale.animateTo(1f, tween(durationMillis = 200, delayMillis = 50))
        }
        launch {
            animatedOffset.animateTo(toOffset, tween(durationMillis = 200))
        }
    }

    val extendedColors = LocalExtendedColors.current
    val colors = remember(num) { extendedColors.getTileColors(num) }

    val textStyle = when  {
        gridSize == 3 -> MaterialTheme.typography.headlineMedium
        gridSize == 4 && !isPortrait -> MaterialTheme.typography.titleLarge
        gridSize == 5 && isPortrait -> MaterialTheme.typography.titleLarge
        gridSize == 5 && !isPortrait -> MaterialTheme.typography.titleMedium
        gridSize == 6 && isPortrait -> MaterialTheme.typography.titleMedium
        gridSize == 6 && !isPortrait -> MaterialTheme.typography.titleSmall
        gridSize == 7 && isPortrait -> MaterialTheme.typography.titleSmall
        gridSize == 7 && !isPortrait -> MaterialTheme.typography.bodySmall
        else -> MaterialTheme.typography.headlineSmall
    }

    Text(
        text = num.toString(),
        modifier = modifier
            .size(size)
            .graphicsLayer(
                scaleX = animatedScale.value,
                scaleY = animatedScale.value,
                translationX = animatedOffset.value.x,
                translationY = animatedOffset.value.y,
            )
            .background(
                color = surfaceColorAtElevation(color = colors.color, elevation = 8.dp),
                shape = RoundedCornerShape(GRID_TILE_RADIUS),
            )
            .wrapContentSize(),
        color = colors.onColor,
        style = textStyle,
        textAlign = TextAlign.Center
    )
}

private fun ExtendedColors.getTileColors(num: Int): CustomColor.ColorRoles {
    val key = when (num) {
        2 -> CustomColor.Key.Tile2
        4 -> CustomColor.Key.Tile4
        8 -> CustomColor.Key.Tile8
        16 -> CustomColor.Key.Tile16
        32 -> CustomColor.Key.Tile32
        64 -> CustomColor.Key.Tile64
        128 -> CustomColor.Key.Tile128
        256 -> CustomColor.Key.Tile256
        512 -> CustomColor.Key.Tile512
        1024 -> CustomColor.Key.Tile1024
        else -> CustomColor.Key.Tile2048 // TODO: Add more colors
    }

    return getColorsByKey(key = key)
}

@Composable
private fun surfaceColorAtElevation(color: Color, elevation: Dp): Color {
    return if (color == MaterialTheme.colorScheme.surface) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(elevation)
    } else {
        color
    }
}

private val GRID_TILE_RADIUS = 4.dp

@Composable
@PreviewLightDark
private fun GridTitleTextPreview() {
    Game2048Theme {
        Surface {
            GridTileText(
                modifier = Modifier.padding(16.dp),
                num = 2,
                size = 100.dp,
                fromScale = 0f,
                fromOffset = Offset(0f, 0f),
                toOffset = Offset(0f, 0f),
                moveCount = 0,
                gridSize = 4,
                isPortrait = true
            )
        }
    }
}
