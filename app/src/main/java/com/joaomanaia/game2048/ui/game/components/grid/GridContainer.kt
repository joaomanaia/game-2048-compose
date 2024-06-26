package com.joaomanaia.game2048.ui.game.components.grid

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
internal fun GridContainer(
    modifier: Modifier = Modifier,
    gridSize: Int,
    content: @Composable @UiComposable BoxWithConstraintsScope.(
        tileSize: Dp,
        tileOffsetPx: Float,
        textFontSize: TextUnit
    ) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f),
    ) {
        val tileWidth = remember(maxWidth, gridSize) {
            calculateTileSize(maxWidth, gridSize)
        }

        val tileOffsetPx = with(LocalDensity.current) {
            tileWidth.toPx() + GRID_ITEM_GAP.toPx()
        }

        val textFontSize = with(LocalDensity.current) {
            (tileWidth * 0.3f).toSp()
        }

        content(tileWidth, tileOffsetPx, textFontSize)
    }
}

internal fun Modifier.drawBackgroundGrid(
    gridSize: Int,
    emptyTileColor: Color
) = drawBehind {
    val tileSizePx = calculateTileSize(size.width.toDp(), gridSize).toPx()
    val tileOffsetPx = tileSizePx + GRID_ITEM_GAP.toPx()

    // Draw the background empty tiles.
    for (row in 0 until gridSize) {
        for (col in 0 until gridSize) {
            drawRoundRect(
                color = emptyTileColor,
                topLeft = Offset(
                    x = col * tileOffsetPx,
                    y = row * tileOffsetPx
                ),
                size = Size(tileSizePx, tileSizePx),
                cornerRadius = CornerRadius(GRID_TILE_RADIUS.toPx()),
            )
        }
    }
}

private fun calculateTileSize(maxWidth: Dp, gridSize: Int): Dp {
    return (maxWidth - GRID_ITEM_GAP * (gridSize - 1)) / gridSize
}

private val GRID_ITEM_GAP = 4.dp
private val GRID_TILE_RADIUS = 4.dp
