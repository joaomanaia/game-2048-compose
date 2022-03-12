package com.infinitepower.game2048.data.game

import com.infinitepower.game2048.core.common.preferences.GameDataPreferencesCommon
import com.infinitepower.game2048.core.datastore.manager.DataStoreManager
import com.infinitepower.game2048.core.di.GameDataPreferences
import com.infinitepower.game2048.domain.SaveGameRepository
import com.infinitepower.game2048.model.Cell
import com.infinitepower.game2048.model.GridTile
import com.infinitepower.game2048.model.GridTileMovement
import com.infinitepower.game2048.model.Tile
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

    private fun createRandomAddedTile(
        grid: List<List<Tile?>>
    ): GridTileMovement? {
        val emptyCells = grid.flatMapIndexed { row, tiles ->
            tiles.mapIndexed { col, tile ->
                if (tile == null) Cell(row, col) else null
            }
        }
        val emptyCell = emptyCells.getOrNull(emptyCells.indices.random()) ?: return null
        return GridTileMovement.add(
            GridTile(
                cell = emptyCell,
                tile = if (Math.random() < 0.9f) Tile(2) else Tile(4)
            )
        )
    }
}