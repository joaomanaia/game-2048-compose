package com.joaomanaia.game2048.presentation.game.components.grid

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joaomanaia.game2048.core.presentation.theme.TileColorsGenerator
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
    textStyle: TextStyle,
    hueParams: TileColorsGenerator.HueParams = TileColorsGenerator.HueParams()
) {
    val containerColor = remember(tile, tileBaseColor, hueParams) {
        TileColorsGenerator.getColorForTile(
            tile = tile,
            baseColor = tileBaseColor,
            hueParams = hueParams
        )
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
            animatedScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
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
        number >= 10_000 -> "${number / 1_000}k"
        else -> number.toString()
    }
}

private val GRID_TILE_RADIUS = 4.dp
