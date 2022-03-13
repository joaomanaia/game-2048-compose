package com.infinitepower.game2048.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.game2048.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.infinitepower.game2048.core.common.GameCommon.NUM_INITIAL_TILES
import com.infinitepower.game2048.core.util.*
import com.infinitepower.game2048.domain.SaveGameRepository
import kotlinx.coroutines.flow.*
import kotlin.math.max

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveGameRepository: SaveGameRepository
) : ViewModel() {
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
            if (saveGameRepository.checkSaveGameExists()) {
                // Restore a previously saved game.
                val savedGrid = saveGameRepository.getSavedGrid()
                grid.emit(savedGrid)

                _homeScreenUiState.emit(
                    homeScreenUiState.first().copy(
                        gridTileMovements = saveGameRepository.getSavedGridTileMovements(),
                        currentScore = saveGameRepository.getSavedCurrentScore(),
                        bestScore = saveGameRepository.getSavedBestScore(),
                        isGameOver = checkIsGameOver(savedGrid)
                    )
                )
            } else {
                startNewGame()
            }
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
        val newBestScore = max(uiState.bestScore, currentNewScore)

        _homeScreenUiState.emit(
            uiState.copy(
                gridTileMovements = newGridTileMovements,
                isGameOver = checkIsGameOver(updatedGrid),
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
}