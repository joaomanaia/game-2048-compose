package com.joaomanaia.game2048.core.util

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isIn
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.joaomanaia.game2048.model.Cell
import com.joaomanaia.game2048.model.Grid
import com.joaomanaia.game2048.model.GridTile
import com.joaomanaia.game2048.model.Tile
import kotlin.test.Test

internal class GameUtilTest {
    @Test
    fun emptyGrid_shouldReturn_emptyGrid() {
        val grid = emptyGrid(4)
        val expectedGrid: Grid = listOf(
            listOf(null, null, null, null),
            listOf(null, null, null, null),
            listOf(null, null, null, null),
            listOf(null, null, null, null)
        )

        assertThat(grid).isEqualTo(expectedGrid)
    }

    @Test
    fun createRandomAddedTile_returnsNull_whenGridIsFull() {
        val grid = listOf(
            listOf(Tile(2), Tile(2), Tile(2), Tile(2)),
            listOf(Tile(2), Tile(2), Tile(2), Tile(2)),
            listOf(Tile(2), Tile(2), Tile(2), Tile(2)),
            listOf(Tile(2), Tile(2), Tile(2), Tile(2))
        )
        val result = createRandomAddedTile(grid)
        assertThat(result).isNull()
    }

    @Test
    fun createRandomAddedTile_returnsAGridTileMovement_whenGridHasEmptyCells() {
        val grid = listOf(
            listOf(Tile(2), Tile(2), Tile(2), Tile(2)),
            listOf(Tile(2), null, Tile(2), Tile(2)),
            listOf(Tile(2), Tile(2), Tile(2), Tile(2)),
            listOf(Tile(2), Tile(2), Tile(2), Tile(2))
        )
        val result = createRandomAddedTile(grid)

        assertAll {
            assertThat(result, "result").isNotNull()
            assertThat(result?.fromGridTile, "fromGridTile").isNull()

            assertThat(result?.toGridTile, "toGridTile").isNotNull()
            assertThat(result?.toGridTile?.tile?.num, "num").isIn(2, 4)
            assertThat(result?.toGridTile?.cell, "cell").isEqualTo(Cell(1, 1))
        }
    }
}
