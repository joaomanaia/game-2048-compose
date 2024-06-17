package com.joaomanaia.game2048.data.repository

import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.manager.DataStoreManager
import com.joaomanaia.game2048.di.GameDataPreferences
import com.joaomanaia.game2048.domain.repository.SaveGameRepository
import com.joaomanaia.game2048.model.Cell
import com.joaomanaia.game2048.model.GridTile
import com.joaomanaia.game2048.model.GridTileMovement
import com.joaomanaia.game2048.model.Tile
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveGameRepositoryImpl @Inject constructor(
    @GameDataPreferences private val gameDataStoreManager: DataStoreManager
) : SaveGameRepository {
    override suspend fun checkSaveGameExists() = getSavedGrid().isNotEmpty()

    override suspend fun getSavedGrid(): List<List<Tile?>> {
        val savedGridStr = gameDataStoreManager.getPreference(GameDataPreferencesCommon.Grid)
        return Json.decodeFromString(savedGridStr)
    }

    override suspend fun getSavedGridTileMovements(): List<GridTileMovement> {
        return getSavedGrid().flatMapIndexed { row, tiles ->
            tiles.mapIndexed { col, tile ->
                if (tile == null) null else GridTileMovement.noop(
                    GridTile(
                        Cell(
                            row,
                            col
                        ), tile
                    )
                )
            }
        }.filterNotNull()
    }

    override suspend fun getSavedCurrentScore(): Int =
        gameDataStoreManager.getPreference(GameDataPreferencesCommon.CurrentScore)

    override suspend fun getSavedBestScore(): Int =
        gameDataStoreManager.getPreference(GameDataPreferencesCommon.BestScore)

    override suspend fun getGridSize(): Int =
        gameDataStoreManager.getPreference(GameDataPreferencesCommon.GridSize)

    override fun getGridSizeFlow(): Flow<Int> =
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.GridSize)

    override suspend fun updateGridSize(newSize: Int) {
        gameDataStoreManager.editPreference(
            key = GameDataPreferencesCommon.GridSize.key,
            newValue = newSize
        )
    }

    override suspend fun saveGame(grid: List<List<Tile?>>, currentScore: Int) {
        val gridStr = Json.encodeToString(grid)

        gameDataStoreManager.editPreferences(
            GameDataPreferencesCommon.Grid.key to gridStr,
            GameDataPreferencesCommon.CurrentScore.key to currentScore
        )
    }

    override suspend fun saveGame(
        grid: List<List<Tile?>>,
        currentScore: Int,
        bestScore: Int
    ) {
        val gridStr = Json.encodeToString(grid)

        gameDataStoreManager.editPreferences(
            GameDataPreferencesCommon.Grid.key to gridStr,
            GameDataPreferencesCommon.CurrentScore.key to currentScore,
            GameDataPreferencesCommon.BestScore.key to bestScore
        )
    }
}