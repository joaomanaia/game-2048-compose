package com.joaomanaia.game2048.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import assertk.assertThat
import assertk.assertions.isCloseTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class HslColorTest {
    companion object {
        private const val TOLERANCE = 0.01f

        @JvmStatic
        fun colorToHslProvider() = listOf(
            Arguments.of(Color.Red.toArgb(), 0f, 1f, 0.5f),
            Arguments.of(Color.Green.toArgb(), 120f, 1f, 0.5f),
            Arguments.of(Color.Blue.toArgb(), 240f, 1f, 0.5f),
            Arguments.of(Color.Black.toArgb(), 0f, 0f, 0f),
            Arguments.of(Color.White.toArgb(), 0f, 0f, 1f),
            Arguments.of(Color(0.5f, 0.5f, 0.5f).toArgb(), 0f, 0f, 0.5f),
            Arguments.of(Color.Yellow.toArgb(), 60f, 1f, 0.5f),
            Arguments.of(Color.Cyan.toArgb(), 180f, 1f, 0.5f),
            Arguments.of(Color.Magenta.toArgb(), 300f, 1f, 0.5f)
        )
    }

    @ParameterizedTest
    @MethodSource("colorToHslProvider")
    fun test_toHsl(
        colorArgb: Int,
        expectedHue: Float,
        expectedSaturation: Float,
        expectedLightness: Float
    ) {
        val hsl = Color(colorArgb).toHsl()

        assertThat(expectedHue, "hue").isCloseTo(hsl.hue, TOLERANCE)
        assertThat(expectedSaturation, "saturation").isCloseTo(hsl.saturation, TOLERANCE)
        assertThat(expectedLightness, "lightness").isCloseTo(hsl.lightness, TOLERANCE)
    }
}
