package com.joaomanaia.game2048.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaomanaia.game2048.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.joaomanaia.game2048.core.common.GameCommon.NUM_INITIAL_TILES
import com.joaomanaia.game2048.core.util.*
import com.joaomanaia.game2048.domain.repository.SaveGameRepository
import kotlinx.coroutines.flow.*
import kotlin.math.max

@HiltViewModel
class GameViewModel @Inject constructor(
    private val saveGameRepository: SaveGameRepository
) : ViewModel() {
    private val _homeScreenUiState = MutableStateFlow(GameScreenUiState())
    val homeScreenUiState = _homeScreenUiState.asStateFlow()

    fun onEvent(event: GameScreenUiEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is GameScreenUiEvent.OnStartNewGameRequest -> startNewGame()
            is GameScreenUiEvent.OnMoveGrid -> move(event.direction)
            is GameScreenUiEvent.OnGridSizeChange -> changeGridSize(event.newSize)
        }
    }

    private val _gridSize = MutableStateFlow(0)
    val gridSize = _gridSize.asStateFlow()

    private val grid = MutableStateFlow<List<List<Tile?>>>(emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            saveGameRepository.getGridSizeFlow().collect { size ->
                _gridSize.emit(size)

                if (saveGameRepository.checkSaveGameExists()) {
                    // Restore a previously saved game.
                    val savedGrid = saveGameRepository.getSavedGrid()
                    if (savedGrid.first().size == size) {
                        grid.emit(savedGrid)

                        _homeScreenUiState.emit(
                            homeScreenUiState.first().copy(
                                gridTileMovements = saveGameRepository.getSavedGridTileMovements(),
                                currentScore = saveGameRepository.getSavedCurrentScore(),
                                bestScore = saveGameRepository.getSavedBestScore(),
                                isGameOver = checkIsGameOver(savedGrid, size)
                            )
                        )
                    } else {
                        startNewGame()
                    }
                } else {
                    startNewGame()
                }
            }
        }
    }

    private suspend fun startNewGame() {
        val newGridTileMovements = (0 until NUM_INITIAL_TILES).mapNotNull {
            createRandomAddedTile(emptyGrid(gridSize.first()))
        }

        val addedGridTiles = newGridTileMovements.map { it.toGridTile }

        val newGrid = emptyGrid(gridSize.first()).map { row, col, _ ->
            addedGridTiles.find { row == it.cell.row && col == it.cell.col }?.tile
        }
        grid.emit(newGrid)

        val uiState = homeScreenUiState.first()
        _homeScreenUiState.emit(
            uiState.copy(
                gridTileMovements = newGridTileMovements,
                currentScore = 0,
                isGameOver = false,
                moveCount = 0
            )
        )

        saveGameRepository.saveGame(newGrid, 0, uiState.bestScore)
    }

    private suspend fun move(direction: Direction) {
        var (updatedGrid, updatedGridTileMovements) = makeMove(grid.first(), direction, gridSize.first())

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

        this@GameViewModel.grid.emit(updatedGrid)

        val newGridTileMovements = updatedGridTileMovements.sortedWith { a, _ -> if (a.fromGridTile == null) 1 else -1 }

        val uiState = homeScreenUiState.first()
        val currentNewScore = uiState.currentScore + scoreIncrement
        val newBestScore = max(uiState.bestScore, currentNewScore)

        _homeScreenUiState.emit(
            uiState.copy(
                gridTileMovements = newGridTileMovements,
                isGameOver = checkIsGameOver(updatedGrid, gridSize.first()),
                bestScore = newBestScore,
                currentScore = currentNewScore
            )
        )
        increaseMoveCount()

        saveGameRepository.saveGame(
            updatedGrid,
            currentNewScore,
            newBestScore
        )
    }

    private suspend fun increaseMoveCount(n: Int = 1) {
        val uiState = homeScreenUiState.first()
        _homeScreenUiState.emit(uiState.copy(moveCount = uiState.moveCount + n))
    }

    private suspend fun changeGridSize(newSize: Int) {
        saveGameRepository.updateGridSize(newSize)
    }
}
