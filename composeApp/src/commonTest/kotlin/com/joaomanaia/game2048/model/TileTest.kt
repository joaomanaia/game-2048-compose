package com.joaomanaia.game2048.model

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlin.test.Test

internal class TileTest {
    @Test
    fun tileNumber_mustBe_aPowerOf2() {
        val tile = Tile(16)

        assertThat(tile.num).isEqualTo(16)
        assertThat(tile.logNum).isEqualTo(4)
    }

    @Test
    fun tileNumber_fails_when_incorrectNumProvided() {
        val exception = assertFailure {
            Tile(15)
        }

        exception
            .isInstanceOf<IllegalArgumentException>()
            .hasMessage("Tile number must be a power of 2.")
    }
}
