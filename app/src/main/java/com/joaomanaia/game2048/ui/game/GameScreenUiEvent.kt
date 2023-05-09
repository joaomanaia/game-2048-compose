package com.joaomanaia.game2048.ui.game

import com.joaomanaia.game2048.model.Direction

sealed class GameScreenUiEvent {
    object OnStartNewGameRequest : GameScreenUiEvent()

    data class OnMoveGrid(
        val direction: Direction
    ) : GameScreenUiEvent()

    data class OnGridSizeChange(
        val newSize: Int
    ) : GameScreenUiEvent()
}
