package com.infinitepower.game2048.model

/**
 * Sealed class describing the 4 different swipe directions.
 */
sealed class Direction {
    companion object {
        fun values(): List<Direction> = listOf(Up, Down, Left, Right)
    }

    object Up : Direction()
    object Down : Direction()
    object Left : Direction()
    object Right : Direction()
}