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
import com.joaomanaia.game2048.core.ui.Game2048Theme
import com.joaomanaia.game2048.core.ui.TileColorsGenerator
import com.joaomanaia.game2048.model.Tile
import kotlinx.coroutines.launch

@Composable
internal fun GridTileText(
    modifier: Modifier = Modifier,
    tile: Tile,
    tileBaseColor: Color = MaterialTheme.colorScheme.primary,
    size: Dp,
    fromScale: Float,
    fromOffset: Offset,
    toOffset: Offset,
    moveCount: Int,
    gridSize: Int,
    isPortrait: Boolean,
) {
    val containerColor = remember(tile, tileBaseColor) {
        TileColorsGenerator.getColorForTile(tile, tileBaseColor)
    }

    GridTileText(
        modifier = modifier,
        num = tile.num,
        size = size,
        fromScale = fromScale,
        fromOffset = fromOffset,
        toOffset = toOffset,
        moveCount = moveCount,
        gridSize = gridSize,
        isPortrait = isPortrait,
        containerColor = containerColor,
        contentColor = Color.White,
    )
}

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
    isPortrait: Boolean,
    containerColor: Color,
    contentColor: Color
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

    GridTileText(
        modifier = modifier.graphicsLayer(
            scaleX = animatedScale.value,
            scaleY = animatedScale.value,
            translationX = animatedOffset.value.x,
            translationY = animatedOffset.value.y,
        ),
        num = num,
        size = size,
        gridSize = gridSize,
        isPortrait = isPortrait,
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

@Composable
internal fun GridTileText(
    modifier: Modifier = Modifier,
    num: Int,
    size: Dp,
    gridSize: Int,
    isPortrait: Boolean,
    containerColor: Color,
    contentColor: Color
) {
    val textStyle = when {
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
            .background(
                color = containerColor,
                shape = RoundedCornerShape(GRID_TILE_RADIUS),
            )
            .wrapContentSize(),
        color = contentColor,
        style = textStyle,
        textAlign = TextAlign.Center
    )
}

private val GRID_TILE_RADIUS = 4.dp

@Composable
@PreviewLightDark
private fun GridTitleTextPreview() {
    Game2048Theme {
        Surface {
            GridTileText(
                modifier = Modifier.padding(16.dp),
                tile = Tile(2),
                size = 100.dp,
                fromScale = 0f,
                fromOffset = Offset(0f, 0f),
                toOffset = Offset(0f, 0f),
                moveCount = 0,
                gridSize = 4,
                isPortrait = true,
            )
        }
    }
}
