package com.infinitepower.game2048.core.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MapUtilTest {
    @Test
    fun `map test`() {
        val list = List(5) {
            List(5) {
                "A"
            }
        }

        val newList = list.map { row, col, s ->
            "$s $row $col"
        }

        newList.forEachIndexed { row, strings ->
            strings.forEachIndexed { col, s ->
                assertEquals("A $row $col", s)
            }
        }
    }
}