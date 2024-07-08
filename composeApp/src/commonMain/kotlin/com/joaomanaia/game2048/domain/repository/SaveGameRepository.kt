package com.joaomanaia.game2048.domain.repository

import com.joaomanaia.game2048.model.Grid
import com.joaomanaia.game2048.model.GridTileMovement
import kotlinx.coroutines.flow.Flow

interface SaveGameRepository {
    suspend fun checkSaveGameExists(): Boolean

    suspend fun getSavedGrid(): Grid

    suspend fun getSavedGridTileMovements(): List<GridTileMovement>

    suspend fun getSavedCurrentScore(): Int

    suspend fun getSavedBestScore(): Int

    suspend fun getGridSize(): Int

    fun getGridSizeFlow(): Flow<Int>

    suspend fun updateGridSize(newSize: Int)

    suspend fun saveGame(
        grid: Grid,
        currentScore: Int
    )

    suspend fun saveGame(
        grid: Grid,
        currentScore: Int,
        bestScore: Int
    )
}
