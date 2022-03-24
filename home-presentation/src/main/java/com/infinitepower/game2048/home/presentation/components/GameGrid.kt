package com.infinitepower.game2048.home.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.infinitepower.game2048.core.ui.CustomColor
import com.infinitepower.game2048.core.ui.LocalExtendedColors
import com.infinitepower.game2048.model.GridTileMovement
import kotlin.math.ln
import kotlin.math.min

@Composable
internal fun GameGrid(
    modifier: Modifier = Modifier,
    gridTileMovements: List<GridTileMovement>,
    moveCount: Int,
    gridSize: Int,
    isPortrait: Boolean
) {
    BoxWithConstraints(modifier) {
        val width = with(LocalDensity.current) { maxWidth.toPx() }
        val height = with(LocalDensity.current) { maxHeight.toPx() }
        val tileMarginPx = with(LocalDensity.current) { 4.dp.toPx() }
        val tileSizePx = ((min(width, height) - tileMarginPx * (gridSize - 1)) / gridSize).coerceAtLeast(0f)
        val tileSizeDp = Dp(tileSizePx / LocalDensity.current.density)
        val tileOffsetPx = tileSizePx + tileMarginPx
        val emptyTileColor = surfaceColorAtElevation(color = MaterialTheme.colorScheme.surface, elevation = 8.dp)
        Box(
            modifier = Modifier.drawBehind {
                // Draw the background empty tiles.
                for (row in 0 until gridSize) {
                    for (col in 0 until gridSize) {
                        drawRoundRect(
                            color = emptyTileColor,
                            topLeft = Offset(col * tileOffsetPx, row * tileOffsetPx),
                            size = Size(tileSizePx, tileSizePx),
                            cornerRadius = CornerRadius(GRID_TILE_RADIUS.toPx()),
                        )
                    }
                }
            }
        ) {
            for (gridTileMovement in gridTileMovements) {
                // Each grid tile is laid out at (0,0) in the box. Shifting tiles are then translated
                // to their correct position in the grid, and added tiles are scaled from 0 to 1.
                val (fromGridTile, toGridTile) = gridTileMovement
                val fromScale = if (fromGridTile == null) 0f else 1f
                val toOffset = Offset(toGridTile.cell.col * tileOffsetPx, toGridTile.cell.row * tileOffsetPx)
                val fromOffset = fromGridTile?.let { Offset(it.cell.col * tileOffsetPx, it.cell.row * tileOffsetPx) } ?: toOffset

                // In 2048, tiles are frequently being removed and added to the grid. As a result,
                // the order in which grid tiles are rendered is constantly changing after each
                // recomposition. In order to ensure that each tile animates from its correct
                // starting position, it is critical that we assign each tile a unique ID using
                // the key() function.
                key(toGridTile.tile.id) {
                    GridTileText(
                        num = toGridTile.tile.num,
                        size = tileSizeDp,
                        fromScale = fromScale,
                        fromOffset = fromOffset,
                        toOffset = toOffset,
                        moveCount = moveCount,
                        gridSize = gridSize,
                        isPortrait = isPortrait
                    )
                }
            }
        }
    }
}

@Composable
private fun GridTileText(
    num: Int,
    size: Dp,
    fromScale: Float,
    fromOffset: Offset,
    toOffset: Offset,
    moveCount: Int,
    gridSize: Int,
    isPortrait: Boolean
) {
    val animatedScale = remember {
        Animatable(fromScale)
    }
    val animatedOffset = remember {
        Animatable(fromOffset, Offset.VectorConverter)
    }

    Text(
        text = "$num",
        modifier = Modifier
            .size(size)
            .graphicsLayer(
                scaleX = animatedScale.value,
                scaleY = animatedScale.value,
                translationX = animatedOffset.value.x,
                translationY = animatedOffset.value.y,
            )
            .background(
                color = surfaceColorAtElevation(color = getTileColor(num), elevation = 8.dp),
                shape = RoundedCornerShape(GRID_TILE_RADIUS),
            )
            .wrapContentSize(),
        color = getTileTextColor(num = num),
        style = when  {
            gridSize == 3 -> MaterialTheme.typography.headlineMedium
            gridSize == 4 && !isPortrait -> MaterialTheme.typography.titleLarge
            gridSize == 5 && isPortrait -> MaterialTheme.typography.titleLarge
            gridSize == 5 && !isPortrait -> MaterialTheme.typography.titleMedium
            gridSize == 6 && isPortrait -> MaterialTheme.typography.titleMedium
            gridSize == 6 && !isPortrait -> MaterialTheme.typography.titleSmall
            gridSize == 7 && isPortrait -> MaterialTheme.typography.titleSmall
            gridSize == 7 && !isPortrait -> MaterialTheme.typography.bodySmall
            else -> MaterialTheme.typography.headlineSmall
        },
        textAlign = TextAlign.Center
    )

    LaunchedEffect(key1 = moveCount) {
        animatedScale.snapTo(if (moveCount == 0) 1f else fromScale)
        animatedScale.animateTo(1f, tween(durationMillis = 200, delayMillis = 50))
        animatedOffset.animateTo(toOffset, tween(durationMillis = 200))
    }
}

@Composable
private fun getTileColor(
    num: Int
): Color {
    val extendedColors = LocalExtendedColors.current

    return when (num) {
        2 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile2)
        4 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile4)
        8 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile8)
        16 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile16)
        32 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile32)
        64 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile64)
        128 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile128)
        256 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile256)
        512 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile512)
        1024 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile1024)
        2048 -> extendedColors.getColorAccentByKey(key = CustomColor.Keys.Tile2048)
        else -> MaterialTheme.colorScheme.surface
    }
}

@Composable
private fun getTileTextColor(
    num: Int
): Color {
    val extendedColors = LocalExtendedColors.current

    return when (num) {
        2 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile2)
        4 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile4)
        8 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile8)
        16 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile16)
        32 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile32)
        64 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile64)
        128 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile128)
        256 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile256)
        512 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile512)
        1024 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile1024)
        2048 -> extendedColors.getColorOnAccentByKey(key = CustomColor.Keys.Tile2048)
        else -> MaterialTheme.colorScheme.inverseSurface
    }
}

@Composable
private fun surfaceColorAtElevation(color: Color, elevation: Dp): Color {
    return if (color == MaterialTheme.colorScheme.surface) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(elevation)
    } else {
        color
    }
}

/**
 * Returns the [ColorScheme.surface] color with an alpha of the [ColorScheme.primary] color overlaid
 * on top of it.
 * Computes the surface tonal color at different elevation levels e.g. surface1 through surface5.
 *
 * @param elevation Elevation value used to compute alpha of the color overlay layer.
 */
private fun ColorScheme.surfaceColorAtElevation(
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return surface
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return primary.copy(alpha = alpha).compositeOver(surface)
}

private val GRID_TILE_RADIUS = 4.dp