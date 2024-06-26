package com.joaomanaia.game2048.ui.game.components.grid

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaomanaia.game2048.core.ui.Game2048Theme
import com.joaomanaia.game2048.core.ui.TileColorsGenerator
import com.joaomanaia.game2048.model.Tile
import kotlinx.coroutines.launch

@Composable
internal fun GridTileText(
    modifier: Modifier = Modifier,
    tile: Tile,
    tileBaseColor: Color = MaterialTheme.colorScheme.primary,
    fromScale: Float,
    fromOffset: Offset,
    toOffset: Offset,
    moveCount: Int,
    textStyle: TextStyle
) {
    val containerColor = remember(tile, tileBaseColor) {
        TileColorsGenerator.getColorForTile(tile, tileBaseColor)
    }

    GridTileText(
        modifier = modifier,
        num = tile.num,
        fromScale = fromScale,
        fromOffset = fromOffset,
        toOffset = toOffset,
        moveCount = moveCount,
        containerColor = containerColor,
        contentColor = Color.White,
        textStyle = textStyle
    )
}

@Composable
internal fun GridTileText(
    modifier: Modifier = Modifier,
    num: Int,
    fromScale: Float,
    fromOffset: Offset,
    toOffset: Offset,
    moveCount: Int,
    textStyle: TextStyle,
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
        textStyle = textStyle,
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

@Composable
internal fun GridTileText(
    modifier: Modifier = Modifier,
    num: Int,
    textStyle: TextStyle,
    containerColor: Color,
    contentColor: Color
) {
    val formattedText = remember(num) { formatNumber(num) }

    Text(
        text = formattedText,
        modifier = modifier
            .drawBehind {
                val radius = GRID_TILE_RADIUS.toPx()

                drawRoundRect(
                    color = containerColor,
                    cornerRadius = CornerRadius(radius, radius)
                )
            }
            .wrapContentSize(),
        color = contentColor,
        textAlign = TextAlign.Center,
        maxLines = 1,
        style = textStyle
    )
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> "${number / 1_000_000}M"
        number >= 1_000 -> "${number / 1_000}k"
        else -> number.toString()
    }
}

private val GRID_TILE_RADIUS = 4.dp

@Composable
@PreviewLightDark
private fun GridTitleTextPreview() {
    Game2048Theme {
        Surface {
            GridTileText(
                modifier = Modifier
                    .padding(16.dp)
                    .size(100.dp),
                tile = Tile(2),
                fromScale = 0f,
                fromOffset = Offset(0f, 0f),
                toOffset = Offset(0f, 0f),
                moveCount = 0,
                textStyle = TextStyle(
                    fontSize = 30.sp
                )
            )
        }
    }
}
