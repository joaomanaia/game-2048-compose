package com.joaomanaia.game2048.core.util

fun <T> List<List<T>>.map(
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
