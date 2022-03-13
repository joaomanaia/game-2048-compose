package com.infinitepower.game2048.core.util

import androidx.annotation.IntRange
import com.google.android.material.math.MathUtils
import com.infinitepower.game2048.core.common.GameCommon.GRID_SIZE
import com.infinitepower.game2048.model.*

fun emptyGrid() = (0 until GRID_SIZE).map {
    arrayOfNulls<Tile?>(GRID_SIZE).toList()
}

fun createRandomAddedTile(
    grid: List<List<Tile?>>
): GridTileMovement? {
    val emptyCells = grid.flatMapIndexed { row, tiles ->
        tiles.mapIndexed { col, tile ->
            if (tile == null) Cell(row, col) else null
        }
    }
    val emptyCell = emptyCells.getOrNull(emptyCells.indices.random()) ?: return null
    return GridTileMovement.add(
        GridTile(
            cell = emptyCell,
            tile = if ((0..10).random() < 9) Tile(2) else Tile(4)
        )
    )
}

fun hasGridChanged(gridTileMovements: List<GridTileMovement>): Boolean {
    // The grid has changed if any of the tiles have moved to a different location.
    return gridTileMovements.any {
        val (fromTile, toTile) = it
        fromTile == null || fromTile.cell != toTile.cell
    }
}

fun checkIsGameOver(grid: List<List<Tile?>>): Boolean {
    // The game is over if no tiles can be moved in any of the 4 directions.
    return Direction.values().none { hasGridChanged(makeMove(grid, it).second) }
}

private fun <T> List<List<T>>.rotate(
    @IntRange(from = 0, to = 3) numRotations: Int
): List<List<T>> {
    return map { row, col, _ ->
        val (rotatedRow, rotatedCol) = getRotatedCellAt(row, col, numRotations)
        this[rotatedRow][rotatedCol]
    }
}

private fun getRotatedCellAt(
    row: Int,
    col: Int,
    @IntRange(from = 0, to = 3) numRotations: Int
): Cell {
    return when (numRotations) {
        0 -> Cell(row, col)
        1 -> Cell(GRID_SIZE - 1 - col, row)
        2 -> Cell(GRID_SIZE - 1 - row, GRID_SIZE - 1 - col)
        3 -> Cell(col, GRID_SIZE - 1 - row)
        else -> throw IllegalArgumentException("numRotations must be an integer in [0,3]")
    }
}

fun makeMove(
    grid: List<List<Tile?>>,
    direction: Direction
): Pair<List<List<Tile?>>, List<GridTileMovement>> {
    val numRotations = when (direction) {
        is Direction.Left -> 0
        is Direction.Down -> 1
        is Direction.Right -> 2
        is Direction.Up -> 3
    }

    // Rotate the grid so that we can process it as if the user has swiped their
    // finger from right to left.
    var updatedGrid = grid.rotate(numRotations)

    val gridTileMovements = mutableListOf<GridTileMovement>()

    updatedGrid = List(updatedGrid.size) { currentRowIndex ->
        val tiles = updatedGrid[currentRowIndex].toMutableList()
        var lastSeenTileIndex: Int? = null
        var lastSeenEmptyIndex: Int? = null
        for (currentColIndex in tiles.indices) {
            val currentTile = tiles[currentColIndex]
            if (currentTile == null) {
                // We are looking at an empty cell in the grid.
                if (lastSeenEmptyIndex == null) {
                    // Keep track of the first empty index we find.
                    lastSeenEmptyIndex = currentColIndex
                }
                continue
            }

            // Otherwise, we have encountered a tile that could either be shifted,
            // merged, or not moved at all.
            val currentGridTile = GridTile(getRotatedCellAt(currentRowIndex, currentColIndex, numRotations), currentTile)

            if (lastSeenTileIndex == null) {
                // This is the first tile in the list that we've found.
                if (lastSeenEmptyIndex == null) {
                    // Keep the tile at its same location.
                    gridTileMovements.add(GridTileMovement.noop(currentGridTile))
                    lastSeenTileIndex = currentColIndex
                } else {
                    // Shift the tile to the location of the furthest empty cell in the list.
                    val targetCell = getRotatedCellAt(currentRowIndex, lastSeenEmptyIndex, numRotations)
                    val targetGridTile = GridTile(targetCell, currentTile)
                    gridTileMovements.add(GridTileMovement.shift(currentGridTile, targetGridTile))

                    tiles[lastSeenEmptyIndex] = currentTile
                    tiles[currentColIndex] = null
                    lastSeenTileIndex = lastSeenEmptyIndex
                    lastSeenEmptyIndex++
                }
            } else {
                // There is a previous tile in the list that we need to process.
                if (tiles[lastSeenTileIndex]!!.num == currentTile.num) {
                    // Shift the tile to the location where it will be merged.
                    val targetCell = getRotatedCellAt(currentRowIndex, lastSeenTileIndex, numRotations)
                    gridTileMovements.add(GridTileMovement.shift(currentGridTile, GridTile(targetCell, currentTile)))

                    // Merge the current tile with the previous tile.
                    val addedTile = currentTile * 2
                    gridTileMovements.add(GridTileMovement.add(GridTile(targetCell, addedTile)))

                    tiles[lastSeenTileIndex] = addedTile
                    tiles[currentColIndex] = null
                    lastSeenTileIndex = null
                    if (lastSeenEmptyIndex == null) {
                        lastSeenEmptyIndex = currentColIndex
                    }
                } else {
                    if (lastSeenEmptyIndex == null) {
                        // Keep the tile at its same location.
                        gridTileMovements.add(GridTileMovement.noop(currentGridTile))
                    } else {
                        // Shift the current tile towards the previous tile.
                        val targetCell = getRotatedCellAt(currentRowIndex, lastSeenEmptyIndex, numRotations)
                        val targetGridTile = GridTile(targetCell, currentTile)
                        gridTileMovements.add(GridTileMovement.shift(currentGridTile, targetGridTile))

                        tiles[lastSeenEmptyIndex] = currentTile
                        tiles[currentColIndex] = null
                        lastSeenEmptyIndex++
                    }
                    lastSeenTileIndex++
                }
            }
        }
        tiles
    }

    // Rotate the grid back to its original state.
    updatedGrid = updatedGrid.rotate(MathUtils.floorMod(-numRotations, GRID_SIZE))

    return Pair(updatedGrid, gridTileMovements)
}