package com.joaomanaia.game2048.model

import androidx.annotation.Keep

/**
 * Container class describing a tile at a certain location in the grid.
 */
@Keep
data class GridTile(
    val cell: Cell,
    val tile: Tile
)
