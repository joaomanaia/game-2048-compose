package com.infinitepower.game2048.model

import androidx.annotation.Keep

/**
 * Container class that describes a location in a 2D grid.
 */
@Keep
data class Cell(
    val row: Int,
    val col: Int
)