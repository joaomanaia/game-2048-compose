package com.joaomanaia.game2048.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

internal class TileTest {
    @Test
    fun `test tile number must be a power of 2`() {
        val tile = assertDoesNotThrow {
            Tile(16)
        }

        assertThat(tile.num).isEqualTo(16)
        assertThat(tile.logNum).isEqualTo(4)
    }

    @Test
    fun `test tile number should throw exception if not a power of 2`() {
        val exception = assertThrows<IllegalArgumentException> {
            Tile(15)
        }

        assertThat(exception.message).isEqualTo("Tile number must be a power of 2.")
    }
}
