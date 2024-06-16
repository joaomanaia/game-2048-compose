package com.joaomanaia.game2048.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * Container class that wraps a number and a unique [id] for use in the grid.
 */
@Keep
@Serializable
data class Tile(
    val num: Int,
    val id: Int
) {
    companion object {
        // We assign each tile a unique ID and use it to efficiently
        // animate tile objects within the compose UI.
        private var tileIdCounter = 0
    }

    constructor(num: Int) : this(num, tileIdCounter++)

    operator fun times(operand: Int): Tile = Tile(num * operand)
}
