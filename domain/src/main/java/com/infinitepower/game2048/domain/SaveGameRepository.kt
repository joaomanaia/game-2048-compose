package com.infinitepower.game2048.domain

import com.infinitepower.game2048.model.GridTileMovement
import com.infinitepower.game2048.model.Tile

interface SaveGameRepository {
    suspend fun checkSaveGameExists(): Boolean

    suspend fun getSavedGrid(): List<List<Tile?>>

    suspend fun getSavedGridTileMovements(): List<GridTileMovement>

    suspend fun getSavedCurrentScore(): Int

    suspend fun getSavedBestScore(): Int

    suspend fun saveGame(
        grid: List<List<Tile?>>,
        currentScore: Int,
        bestScore: Int
    )
}