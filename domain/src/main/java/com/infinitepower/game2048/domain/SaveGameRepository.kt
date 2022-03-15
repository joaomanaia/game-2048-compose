package com.infinitepower.game2048.domain

import com.infinitepower.game2048.model.GridTileMovement
import com.infinitepower.game2048.model.Tile
import kotlinx.coroutines.flow.Flow

interface SaveGameRepository {
    suspend fun checkSaveGameExists(): Boolean

    suspend fun getSavedGrid(): List<List<Tile?>>

    suspend fun getSavedGridTileMovements(): List<GridTileMovement>

    suspend fun getSavedCurrentScore(): Int

    suspend fun getSavedBestScore(): Int

    suspend fun getGridSize(): Int

    fun getGridSizeFlow(): Flow<Int>

    suspend fun saveGame(
        grid: List<List<Tile?>>,
        currentScore: Int
    )

    suspend fun saveGame(
        grid: List<List<Tile?>>,
        currentScore: Int,
        bestScore: Int
    )
}