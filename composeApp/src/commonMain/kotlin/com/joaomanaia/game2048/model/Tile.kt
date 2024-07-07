package com.joaomanaia.game2048.model

import kotlinx.serialization.Serializable
import kotlin.math.log2
import kotlin.math.roundToInt

/**
 * Container class that wraps a number and a unique [id] for use in the grid.
 */
@Serializable
data class Tile(
    val num: Int,
    val id: Int
) {
    init {
        // Check if the number is valid, by checking if it is a power of 2.
        require(num and (num - 1) == 0) { "Tile number must be a power of 2." }
    }

    companion object {
        // We assign each tile a unique ID and use it to efficiently
        // animate tile objects within the compose UI.
        internal var tileIdCounter = 0
    }

    constructor(num: Int) : this(num, tileIdCounter++)

    operator fun times(operand: Int): Tile = Tile(num * operand)

    val logNum: Int
        get() = log2(num.toFloat()).roundToInt()
}
