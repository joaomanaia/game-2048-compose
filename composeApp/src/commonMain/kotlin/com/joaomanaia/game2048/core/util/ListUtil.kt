package com.joaomanaia.game2048.core.util

import androidx.annotation.IntRange
import com.joaomanaia.game2048.model.Cell

internal fun <T> List<List<T>>.map(
    transform: (
        row: Int,
        col: Int,
        T
    ) -> T
): List<List<T>> = mapIndexed { row, rowTiles ->
    rowTiles.mapIndexed { col, colTile ->
        transform(row, col, colTile)
    }
}

internal fun <T> List<List<T>>.rotate(
    @IntRange(from = 0, to = 3) numRotations: Int
): List<List<T>> {
    require(numRotations in 0..3) { "numRotations must be an integer in 0..3" }

    return map { row, col, _ ->
        val (rotatedRow, rotatedCol) = getRotatedCellAt(
            row = row,
            col = col,
            numRotations = numRotations,
            gridSize = size
        )
        this[rotatedRow][rotatedCol]
    }
}

internal fun getRotatedCellAt(
    row: Int,
    col: Int,
    @IntRange(from = 0, to = 3) numRotations: Int,
    gridSize: Int
): Cell {
    return when (numRotations) {
        0 -> Cell(row, col)
        1 -> Cell(gridSize - 1 - col, row)
        2 -> Cell(gridSize - 1 - row, gridSize - 1 - col)
        3 -> Cell(col, gridSize - 1 - row)
        else -> throw IllegalArgumentException("numRotations must be a integer in 0..3")
    }
}
