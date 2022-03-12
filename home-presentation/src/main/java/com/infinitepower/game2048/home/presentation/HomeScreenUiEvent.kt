package com.infinitepower.game2048.home.presentation

import com.infinitepower.game2048.model.Direction

sealed class HomeScreenUiEvent {
    object OnStartNewGameClick : HomeScreenUiEvent()

    data class OnMoveGrid(
        val direction: Direction
    ) : HomeScreenUiEvent()
}
