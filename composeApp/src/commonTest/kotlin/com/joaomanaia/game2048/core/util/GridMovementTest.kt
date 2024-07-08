package com.joaomanaia.game2048.core.util

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.joaomanaia.game2048.model.Cell
import com.joaomanaia.game2048.model.Direction
import com.joaomanaia.game2048.model.Grid
import com.joaomanaia.game2048.model.GridTile
import com.joaomanaia.game2048.model.GridTileMovement
import com.joaomanaia.game2048.model.Tile
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class GridMovementTest {
    private lateinit var initialGrid: Grid

    @BeforeTest
    fun setup() {
        Tile.tileIdCounter = 0

        // The tile ids are dynamic generated, the table of generated ids are:
        // | 0 | - | - |
        // | 1 | 2 | - |
        // | 3 | 4 | - |
        initialGrid = listOf(
            listOf(Tile(16), null, null),
            listOf(Tile(2), Tile(2), null),
            listOf(Tile(8), Tile(4), null)
        )
    }

    @Test
    fun testEmptyGrid() {
        val grid = emptyGrid(3)

        for (direction in Direction.entries) {
            val (newGrid, movements) = grid.makeMove(direction)

            assertThat(newGrid.isGridEmpty()).isTrue()
            assertThat(movements).isEmpty()
        }
    }

    @Test
    fun testDownTileMovement() {
        initialGrid.makeMove(Direction.DOWN).also { (newGridDown, movementsDown) ->
            assertThat(newGridDown).isEqualTo(initialGrid)
            with(initialGrid) {
                assertThat(movementsDown).containsExactlyInAnyOrder(
                    createNoopMovementFromGrid(0, 0),
                    createNoopMovementFromGrid(1, 0),
                    createNoopMovementFromGrid(1, 1),
                    createNoopMovementFromGrid(2, 0),
                    createNoopMovementFromGrid(2, 1),
                )
            }
        }
    }

    @Test
    fun testUpTileMovement() {
        initialGrid.makeMove(Direction.UP).also { (newGridUp, movementsUp) ->
            assertThat(newGridUp).isEqualTo(
                listOf(
                    listOf(Tile(16, 0), Tile(2, 2), null),
                    listOf(Tile(2, 1), Tile(4, 4), null),
                    listOf(Tile(8, 3), null, null),
                )
            )
            with(initialGrid) {
                assertThat(movementsUp).containsExactlyInAnyOrder(
                    createNoopMovementFromGrid(0, 0),
                    createNoopMovementFromGrid(1, 0),
                    createNoopMovementFromGrid(2, 0),
                    createShiftMovementFromGrid(1, 1, 0, 1),
                    createShiftMovementFromGrid(2, 1, 1, 1),
                )
            }
        }
    }

    @Test
    fun testLeftTileMovement() {
        initialGrid.makeMove(Direction.LEFT).also { (newGridLeft, movementsLeft) ->
            assertThat(newGridLeft).isEqualTo(
                listOf(
                    listOf(Tile(16, 0), null, null),
                    listOf(Tile(4, 5), null, null),
                    listOf(Tile(8, 3), Tile(4, 4), null)
                )
            )
            with(initialGrid) {
                assertThat(movementsLeft).containsExactlyInAnyOrder(
                    createNoopMovementFromGrid(0, 0),
                    createNoopMovementFromGrid(2, 0),
                    createNoopMovementFromGrid(2, 1),
                    createNoopMovementFromGrid(1, 0),
                    createAddMovementFromGrid(4, 5,1, 0),
                    createShiftMovementFromGrid(1, 1, 1, 0)
                )
            }
        }
    }

    @Test
    fun testRightTileMovement() {
        initialGrid.makeMove(Direction.RIGHT).also { (newGridRight, movementsRight) ->
            assertThat(newGridRight).isEqualTo(
                listOf(
                    listOf(null, null, Tile(16, 0)),
                    listOf(null, null, Tile(4, 5)),
                    listOf(null, Tile(8, 3), Tile(4, 4))
                )
            )
            with(initialGrid) {
                assertThat(movementsRight).containsExactlyInAnyOrder(
                    createShiftMovementFromGrid(0, 0, 0, 2),
                    createShiftMovementFromGrid(2, 1, 2, 2),
                    createShiftMovementFromGrid(2, 0, 2, 1),
                    createShiftMovementFromGrid(1, 1, 1, 2),
                    createShiftMovementFromGrid(1, 0, 1, 2),
                    createAddMovementFromGrid(4, 5, 1, 2),
                )
            }
        }
    }

    private fun createAddMovementFromGrid(tileNum: Int, tileId: Int, row: Int, col: Int): GridTileMovement {
        return GridTileMovement.add(GridTile(Cell(row, col), Tile(tileNum, tileId)))
    }

    private fun Grid.createShiftMovementFromGrid(
        fromRow: Int,
        fromCol: Int,
        toRow: Int,
        toCol: Int
    ): GridTileMovement {
        val tile = this[fromRow][fromCol]!!

        return GridTileMovement.shift(
            fromGridTile = GridTile(Cell(fromRow, fromCol), tile),
            toGridTile = GridTile(Cell(toRow, toCol), tile)
        )
    }

    private fun Grid.createNoopMovementFromGrid(row: Int, col: Int): GridTileMovement {
        return GridTileMovement.noop(GridTile(Cell(row, col), this[row][col]!!))
    }
}
