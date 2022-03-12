package com.infinitepower.game2048.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.game2048.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import androidx.annotation.IntRange
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.material.math.MathUtils.floorMod
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState = _homeScreenUiState.asStateFlow()

    fun onEvent(event: HomeScreenUiEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is HomeScreenUiEvent.OnStartNewGameClick -> startNewGame()
            is HomeScreenUiEvent.OnMoveGrid -> move(event.direction)
        }
    }

    private val grid = MutableStateFlow(emptyGrid())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            startNewGame()
        }
    }

    private suspend fun startNewGame() {
        val newGridTileMovements = (0 until NUM_INITIAL_TILES).mapNotNull {
            createRandomAddedTile(emptyGrid())
        }

        val addedGridTiles = newGridTileMovements.map { it.toGridTile }

        val newGrid = emptyGrid().map { row, col, _ ->
            addedGridTiles.find { row == it.cell.row && col == it.cell.col }?.tile
        }
        grid.emit(newGrid)

        _homeScreenUiState.emit(
            homeScreenUiState.first().copy(
                gridTileMovements = newGridTileMovements,
                currentScore = 0,
                isGameOver = false,
                moveCount = 0
            )
        )
    }

    private suspend fun move(direction: Direction) {
        var (updatedGrid, updatedGridTileMovements) = makeMove(grid.first(), direction)

        if (!hasGridChanged(updatedGridTileMovements)) return // No tiles were moved.

        // Increment the score
        val scoreIncrement = updatedGridTileMovements.filter { it.fromGridTile == null }.sumOf { it.toGridTile.tile.num }

        updatedGridTileMovements = updatedGridTileMovements.toMutableList()
        val addedTileMovement = createRandomAddedTile(updatedGrid)
        if (addedTileMovement != null) {
            val (cell, tile) = addedTileMovement.toGridTile
            updatedGrid = updatedGrid.map { r, c, it -> if (cell.row == r && cell.col == c) tile else it }
            updatedGridTileMovements.add(addedTileMovement)
        }

        this@HomeViewModel.grid.emit(updatedGrid)

        val newGridTileMovements = updatedGridTileMovements.sortedWith { a, _ -> if (a.fromGridTile == null) 1 else -1 }

        val uiState = homeScreenUiState.first()
        val currentNewScore = uiState.currentScore + scoreIncrement

        _homeScreenUiState.emit(
            uiState.copy(
                gridTileMovements = newGridTileMovements,
                isGameOver = checkIsGameOver(updatedGrid),
                bestScore = max(uiState.bestScore, currentNewScore),
                currentScore = currentNewScore
            )
        )
        increaseMoveCount()
    }

    private suspend fun increaseMoveCount(n: Int = 1) {
        val uiState = homeScreenUiState.first()
        _homeScreenUiState.emit(uiState.copy(moveCount = uiState.moveCount + n))
    }
}

const val GRID_SIZE = 4
const val NUM_INITIAL_TILES = 2

private fun makeMove(
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
    updatedGrid = updatedGrid.rotate(floorMod(-numRotations, GRID_SIZE))

    return Pair(updatedGrid, gridTileMovements)
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
            tile = if (Math.random() < 0.9f) Tile(2) else Tile(4)
        )
    )
}

fun emptyGrid() = (0 until GRID_SIZE).map {
    arrayOfNulls<Tile?>(GRID_SIZE).toList()
}

private fun <T> List<List<T>>.map(
    transform: (
        row: Int,
        col: Int,
        T
    ) -> T
): List<List<T>> {
    return mapIndexed { row, rowTiles ->
        rowTiles.mapIndexed { col, colTiles ->
            transform(row, col, colTiles)
        }
    }
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

private fun checkIsGameOver(grid: List<List<Tile?>>): Boolean {
    // The game is over if no tiles can be moved in any of the 4 directions.
    return Direction.values().none { hasGridChanged(makeMove(grid, it).second) }
}

private fun hasGridChanged(gridTileMovements: List<GridTileMovement>): Boolean {
    // The grid has changed if any of the tiles have moved to a different location.
    return gridTileMovements.any {
        val (fromTile, toTile) = it
        fromTile == null || fromTile.cell != toTile.cell
    }
}