package com.infinitepower.game2048.home.presentation

import androidx.annotation.Keep
import com.infinitepower.game2048.model.GridTileMovement

@Keep
data class HomeScreenUiState(
    val gridTileMovements: List<GridTileMovement> = emptyList(),
    val currentScore: Int = 0,
    val bestScore: Int = 0,
    val moveCount: Int = 0,
    val isGameOver: Boolean = false,
)