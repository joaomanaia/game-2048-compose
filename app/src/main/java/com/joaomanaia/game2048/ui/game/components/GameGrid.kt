package com.joaomanaia.game2048.ui.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import com.joaomanaia.game2048.core.ui.spacing
import com.joaomanaia.game2048.model.GridTileMovement
import com.joaomanaia.game2048.ui.game.components.grid.GridContainer
import com.joaomanaia.game2048.ui.game.components.grid.GridTileText
import com.joaomanaia.game2048.ui.game.components.grid.drawBackgroundGrid

@Composable
internal fun GameGrid(
    modifier: Modifier = Modifier,
    gridTileMovements: List<GridTileMovement>,
    moveCount: Int,
    gridSize: Int,
) {
    GridContainer(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacing.medium)
            .clip(MaterialTheme.shapes.large)
            .drawBackgroundGrid(
                gridSize = gridSize,
                emptyTileColor = MaterialTheme.colorScheme.surfaceVariant
            ),
        gridSize = gridSize
    ) { tileSize, tileOffsetPx, textFontSize ->
        for (gridTileMovement in gridTileMovements) {
            // Each grid tile is laid out at (0,0) in the box. Shifting tiles are then translated
            // to their correct position in the grid, and added tiles are scaled from 0 to 1.
            val (fromGridTile, toGridTile) = gridTileMovement
            val fromScale = if (fromGridTile == null) 0f else 1f
            val toOffset = Offset(
                x = toGridTile.cell.col * tileOffsetPx,
                y = toGridTile.cell.row * tileOffsetPx
            )
            val fromOffset = fromGridTile?.let {
                Offset(x = it.cell.col * tileOffsetPx, y = it.cell.row * tileOffsetPx)
            } ?: toOffset

            // In 2048, tiles are frequently being removed and added to the grid. As a result,
            // the order in which grid tiles are rendered is constantly changing after each
            // recomposition. In order to ensure that each tile animates from its correct
            // starting position, it is critical that we assign each tile a unique ID using
            // the key() function.
            key(toGridTile.tile.id) {
                GridTileText(
                    modifier = Modifier.size(tileSize),
                    tile = toGridTile.tile,
                    fromScale = fromScale,
                    fromOffset = fromOffset,
                    toOffset = toOffset,
                    moveCount = moveCount,
                    textStyle = TextStyle(fontSize = textFontSize)
                )
            }
        }
    }
}