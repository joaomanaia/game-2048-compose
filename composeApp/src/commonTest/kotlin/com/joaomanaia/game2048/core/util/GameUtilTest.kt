package com.joaomanaia.game2048.core.util

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isIn
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.joaomanaia.game2048.model.Cell
import com.joaomanaia.game2048.model.Grid
import com.joaomanaia.game2048.model.GridTile
import com.joaomanaia.game2048.model.GridTileMovement
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

    @Test
    fun test_hasGridChanged() {
        var movements = listOf<GridTileMovement>()

        // Grid has not changed when the grid is empty
        assertThat(hasGridChanged(movements)).isFalse()

        val tile1 = GridTile(Cell(0, 0), Tile(2))
        val tile2 = GridTile(Cell(2, 2), Tile(4))

        // Grid has changed when a cell is added
        movements = listOf(
            GridTileMovement(fromGridTile = null, toGridTile = tile1),
            GridTileMovement(fromGridTile = null, toGridTile = tile2),
        )
        assertThat(hasGridChanged(movements)).isTrue()

        // Grid has changed when no cells are changed or added
        movements = listOf(
            GridTileMovement(fromGridTile = tile1, toGridTile = tile1),
            GridTileMovement(fromGridTile = tile2, toGridTile = tile2)
        )
        assertThat(hasGridChanged(movements)).isFalse()

        // Grid has changed when a tile position is changed
        movements = listOf(
            GridTileMovement(
                fromGridTile = tile1,
                toGridTile = tile1.copy(Cell(1, 1))
            ),
            GridTileMovement(fromGridTile = tile2, toGridTile = tile2)
        )
        assertThat(hasGridChanged(movements)).isTrue()
    }

    @Test
    fun testGameOver_noMovesPossible_returnsTrue() {
        val grid: Grid = listOf(
            listOf(Tile(2), Tile(4), Tile(8)),
            listOf(Tile(16), Tile(32), Tile(64)),
            listOf(Tile(128), Tile(256), Tile(512))
        )

        val isGameOver = checkIsGameOver(grid)
        assertThat(isGameOver).isTrue()
    }

    @Test
    fun testGameOver_emptyGrid_returnsFalse() {
        val isGameOver = checkIsGameOver(emptyGrid(3))
        assertThat(isGameOver).isFalse()
    }

    @Test
    fun testGameOver_withPossibleMoves_returnsFalse() {
        val grid: Grid = listOf(
            listOf(Tile(2), null, null),
            listOf(Tile(4), null, null),
            listOf(Tile(8), null, null)
        )

        val isGameOver = checkIsGameOver(grid)
        assertThat(isGameOver).isFalse()
    }
}
