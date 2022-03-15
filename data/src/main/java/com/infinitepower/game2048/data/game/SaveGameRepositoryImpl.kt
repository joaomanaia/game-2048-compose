package com.infinitepower.game2048.data.game

import android.util.Log
import com.infinitepower.game2048.core.common.preferences.GameDataPreferencesCommon
import com.infinitepower.game2048.core.datastore.manager.DataStoreManager
import com.infinitepower.game2048.core.di.GameDataPreferences
import com.infinitepower.game2048.domain.SaveGameRepository
import com.infinitepower.game2048.model.Cell
import com.infinitepower.game2048.model.GridTile
import com.infinitepower.game2048.model.GridTileMovement
import com.infinitepower.game2048.model.Tile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SaveGameRepositoryImpl(
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
                if (tile == null) null else GridTileMovement.noop(GridTile(Cell(row, col), tile))
            }
        }.filterNotNull()
    }

    override suspend fun getSavedCurrentScore(): Int =
        gameDataStoreManager.getPreference(GameDataPreferencesCommon.CurrentScore)

    override suspend fun getSavedBestScore(): Int =
        gameDataStoreManager.getPreference(GameDataPreferencesCommon.BestScore)

    override suspend fun getGridSize(): Int =
        gameDataStoreManager.getPreference(GameDataPreferencesCommon.GridSize).toIntOrNull() ?: 4

    override fun getGridSizeFlow(): Flow<Int> =
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.GridSize)
            .map { sizeStr ->
                sizeStr.toIntOrNull() ?: 4
            }

    override suspend fun saveGame(grid: List<List<Tile?>>, currentScore: Int) {
        val gridStr = Json.encodeToString(grid)
        gameDataStoreManager.editPreference(
            key = GameDataPreferencesCommon.Grid.key,
            newValue = gridStr
        )
        gameDataStoreManager.editPreference(
            key = GameDataPreferencesCommon.CurrentScore.key,
            newValue = currentScore
        )
    }

    override suspend fun saveGame(
        grid: List<List<Tile?>>,
        currentScore: Int,
        bestScore: Int
    ) {
        val gridStr = Json.encodeToString(grid)
        gameDataStoreManager.editPreference(
            key = GameDataPreferencesCommon.Grid.key,
            newValue = gridStr
        )
        gameDataStoreManager.editPreference(
            key = GameDataPreferencesCommon.CurrentScore.key,
            newValue = currentScore
        )
        gameDataStoreManager.editPreference(
            key = GameDataPreferencesCommon.BestScore.key,
            newValue = bestScore
        )
    }
}