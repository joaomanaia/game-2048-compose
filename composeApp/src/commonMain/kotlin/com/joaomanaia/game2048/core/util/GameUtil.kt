package com.joaomanaia.game2048.core.util

import com.joaomanaia.game2048.model.Cell
import com.joaomanaia.game2048.model.Direction
import com.joaomanaia.game2048.model.Grid
import com.joaomanaia.game2048.model.GridTile
import com.joaomanaia.game2048.model.GridTileMovement
import com.joaomanaia.game2048.model.Tile
import kotlin.random.Random

/**
 * Returns an empty grid of the specified [gridSize].
 */
fun emptyGrid(gridSize: Int): Grid = List(gridSize) {
    List(gridSize) { null }
}

fun Grid.isGridEmpty(): Boolean {
    return all { row -> row.all { it == null } }
}

fun createRandomAddedTile(grid: Grid): GridTileMovement? {
    val emptyCells = grid.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, tile ->
            if (tile == null) Cell(rowIndex, colIndex) else null
        }
    }

    // If there are no empty cells, return null
    if (emptyCells.isEmpty()) return null

    val randomEmptyCell = emptyCells.random()
    val newTileNum = generateRandomTileNum()

    return GridTileMovement.add(GridTile(cell = randomEmptyCell, tile = Tile(newTileNum)))
}

private fun generateRandomTileNum(): Int {
    return if (Random.nextFloat() < PROBABILITY_OF_TWO) 2 else 4
}

/**
 * Generate a random tile num (90% chance of 2, 10% chance of 4)
 */
private const val PROBABILITY_OF_TWO = 0.9f

fun hasGridChanged(gridTileMovements: List<GridTileMovement>): Boolean {
    // The grid has changed if any of the tiles have moved to a different location.
    return gridTileMovements.any { (fromTile, toTile) ->
        fromTile == null || fromTile.cell != toTile.cell
    }
}

fun checkIsGameOver(grid: Grid): Boolean {
    if (grid.isGridEmpty()) return false

    // The game is over if no tiles can be moved in any of the 4 directions.
    return Direction.entries.none { hasGridChanged(grid.makeMove(it).second) }
}

fun Grid.makeMove(direction: Direction): Pair<Grid, List<GridTileMovement>> {
    val numRotations = when (direction) {
        Direction.LEFT -> 0
        Direction.DOWN -> 1
        Direction.RIGHT -> 2
        Direction.UP -> 3
    }

    // Rotate the grid so that we can process it as if the user has swiped their
    // finger from right to left.
    var updatedGrid = rotate(numRotations = numRotations)

    val gridTileMovements = mutableListOf<GridTileMovement>()

    updatedGrid = List(size) { currentRowIndex ->
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
            val currentGridTile = GridTile(
                getRotatedCellAt(
                    row = currentRowIndex,
                    col = currentColIndex,
                    numRotations = numRotations,
                    gridSize = size
                ),
                currentTile
            )

            if (lastSeenTileIndex == null) {
                // This is the first tile in the list that we've found.
                if (lastSeenEmptyIndex == null) {
                    // Keep the tile at its same location.
                    gridTileMovements.add(GridTileMovement.noop(currentGridTile))
                    lastSeenTileIndex = currentColIndex
                } else {
                    // Shift the tile to the location of the furthest empty cell in the list.
                    val targetCell = getRotatedCellAt(
                        row = currentRowIndex,
                        col = lastSeenEmptyIndex,
                        numRotations = numRotations,
                        gridSize = size
                    )
                    val targetGridTile =
                        GridTile(targetCell, currentTile)
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
                    val targetCell = getRotatedCellAt(
                        row = currentRowIndex,
                        col = lastSeenTileIndex,
                        numRotations = numRotations,
                        gridSize = size
                    )
                    gridTileMovements.add(
                        GridTileMovement.shift(
                            currentGridTile,
                            GridTile(targetCell, currentTile)
                        )
                    )

                    // Merge the current tile with the previous tile.
                    val addedTile = currentTile * 2
                    gridTileMovements.add(
                        GridTileMovement.add(
                            GridTile(
                                targetCell,
                                addedTile
                            )
                        )
                    )

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
                        val targetCell = getRotatedCellAt(
                            row = currentRowIndex,
                            col = lastSeenEmptyIndex,
                            numRotations = numRotations,
                            gridSize = size
                        )
                        val targetGridTile =
                            GridTile(targetCell, currentTile)
                        gridTileMovements.add(
                            GridTileMovement.shift(
                                currentGridTile,
                                targetGridTile
                            )
                        )

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
    updatedGrid = updatedGrid.rotate(numRotations = floorMod(-numRotations, 4))

    return Pair(updatedGrid, gridTileMovements)
}

private fun floorMod(x: Int, y: Int): Int {
    var r = x / y
    // if the signs are different and modulo not zero, round down
    if ((x xor y) < 0 && (r * y != x)) {
        r--
    }
    return x - r * y
}
