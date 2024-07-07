package com.joaomanaia.game2048.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.joaomanaia.game2048.core.common.GameCommon.NUM_INITIAL_TILES
import com.joaomanaia.game2048.core.util.checkIsGameOver
import com.joaomanaia.game2048.core.util.createRandomAddedTile
import com.joaomanaia.game2048.core.util.emptyGrid
import com.joaomanaia.game2048.core.util.hasGridChanged
import com.joaomanaia.game2048.core.util.makeMove
import com.joaomanaia.game2048.core.util.map
import com.joaomanaia.game2048.domain.repository.SaveGameRepository
import com.joaomanaia.game2048.domain.usecase.GetHueParamsUseCase
import com.joaomanaia.game2048.model.Direction
import kotlinx.coroutines.flow.*
import kotlin.math.max

class GameViewModel(
    private val saveGameRepository: SaveGameRepository,
    getHueParamsUseCase: GetHueParamsUseCase
) : ViewModel() {
    private val _homeScreenUiState = MutableStateFlow(GameScreenUiState())
    val homeScreenUiState = combine(
        _homeScreenUiState,
        getHueParamsUseCase()
    ) { uiState, hueParams ->
        uiState.copy(hueParams = hueParams)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameScreenUiState()
    )

    fun onEvent(event: GameScreenUiEvent) {
        viewModelScope.launch {
            when (event) {
                is GameScreenUiEvent.OnStartNewGameRequest -> startNewGame()
                is GameScreenUiEvent.OnMoveGrid -> move(event.direction)
                is GameScreenUiEvent.OnGridSizeChange -> changeGridSize(event.newSize)
            }
        }
    }

    init {
        saveGameRepository
            .getGridSizeFlow()
            .onEach { gridSize ->
                val savedGrid = saveGameRepository.getSavedGrid()
                val isGameSaved = savedGrid.isNotEmpty()

                // Restore a previously saved game if there is one and the grid size matches.
                if (isGameSaved && savedGrid.first().size == gridSize) {
                    _homeScreenUiState.update { state ->
                        state.copy(
                            gridSize = gridSize,
                            grid = savedGrid,
                            gridTileMovements = saveGameRepository.getSavedGridTileMovements(),
                            currentScore = saveGameRepository.getSavedCurrentScore(),
                            bestScore = saveGameRepository.getSavedBestScore(),
                            isGameOver = checkIsGameOver(savedGrid),
                            moveCount = 0
                        )
                    }
                } else {
                    // If no saved game, start a new game.
                    startNewGame(gridSize)
                }
            }
            .launchIn(viewModelScope)

        homeScreenUiState
            .filterNot { state ->
                state.isGameOver
                        || state.gridTileMovements.isEmpty()
                        || state.grid.isEmpty()
                        || state.currentScore == 0
                        || state.bestScore == 0
            }.onEach { state ->
                saveGameRepository.saveGame(
                    grid = state.grid,
                    currentScore = state.currentScore,
                    bestScore = state.bestScore
                )
            }.launchIn(viewModelScope)
    }

    private fun startNewGame(gridSize: Int? = null) {
        _homeScreenUiState.update { state ->
            val emptyGrid = emptyGrid(gridSize ?: state.gridSize)

            val newGridTileMovements = (0 until NUM_INITIAL_TILES).mapNotNull {
                createRandomAddedTile(emptyGrid)
            }

            val addedGridTiles = newGridTileMovements.map { it.toGridTile }

            val newGrid = emptyGrid.map { row, col, _ ->
                addedGridTiles.find { row == it.cell.row && col == it.cell.col }?.tile
            }

            state.copy(
                // If the gridSize if provided, use it, otherwise use the current gridSize.
                gridSize = gridSize ?: state.gridSize,
                grid = newGrid,
                gridTileMovements = newGridTileMovements,
                currentScore = 0,
                isGameOver = false,
                moveCount = 0
            )
        }
    }

    private fun move(direction: Direction) {
        _homeScreenUiState.update { state ->
            var (updatedGrid, updatedGridTileMovements) = makeMove(
                grid = state.grid,
                direction = direction,
            )

            if (!hasGridChanged(updatedGridTileMovements)) return // No tiles were moved.

            // Increment the score
            val scoreIncrement = updatedGridTileMovements
                .filter { it.fromGridTile == null }
                .sumOf { it.toGridTile.tile.num }

            updatedGridTileMovements = updatedGridTileMovements.toMutableList()
            val addedTileMovement = createRandomAddedTile(updatedGrid)
            if (addedTileMovement != null) {
                val (cell, tile) = addedTileMovement.toGridTile
                updatedGrid =
                    updatedGrid.map { r, c, it -> if (cell.row == r && cell.col == c) tile else it }
                updatedGridTileMovements.add(addedTileMovement)
            }

            val newGridTileMovements =
                updatedGridTileMovements.sortedWith { a, _ -> if (a.fromGridTile == null) 1 else -1 }

            val currentNewScore = state.currentScore + scoreIncrement
            val newBestScore = max(state.bestScore, currentNewScore)

            state.copy(
                grid = updatedGrid,
                gridTileMovements = newGridTileMovements,
                isGameOver = checkIsGameOver(updatedGrid),
                bestScore = newBestScore,
                currentScore = currentNewScore,
                moveCount = state.moveCount + 1
            )
        }
    }

    private suspend fun changeGridSize(newSize: Int) {
        saveGameRepository.updateGridSize(newSize)
    }
}
