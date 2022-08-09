package com.joaomanaia.game2048.ui.game

import androidx.annotation.Keep
import com.joaomanaia.game2048.model.GridTileMovement

@Keep
data class GameScreenUiState(
    val gridTileMovements: List<GridTileMovement> = emptyList(),
    val currentScore: Int = 0,
    val bestScore: Int = 0,
    val moveCount: Int = 0,
    val isGameOver: Boolean = false,
)