package com.joaomanaia.game2048.core.util

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import kotlin.test.Test

internal class ListRotationTest {
    @Test
    fun test_emptyList() {
        val emptyList = emptyList<List<Int>>()
        val rotatedList = emptyList.rotate(numRotations = 1)

        assertThat(rotatedList).isEmpty()
        assertThat(rotatedList).isEqualTo(emptyList)
    }

    @Test
    fun test_singleElementList() {
        val singleElementList = listOf(listOf(1))

        repeat(4) {
            val rotatedList = singleElementList.rotate(numRotations = it)
            assertThat(rotatedList).isEqualTo(singleElementList)
        }
    }

    @Test
    fun test_90DegreeRotationOnSquareGrid() {
        val originalList = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9)
        )
        val expectedList = listOf(
            listOf(7, 4, 1),
            listOf(8, 5, 2),
            listOf(9, 6, 3)
        )
        val rotatedList = originalList.rotate(numRotations = 1)
        assertThat(rotatedList).isEqualTo(expectedList)
    }
}
