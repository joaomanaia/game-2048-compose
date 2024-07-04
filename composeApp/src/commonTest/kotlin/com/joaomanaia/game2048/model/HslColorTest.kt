package com.joaomanaia.game2048.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import assertk.assertThat
import assertk.assertions.isCloseTo
import kotlin.test.Test

internal class HslColorTest {
    companion object {
        private const val TOLERANCE = 0.01f

        private data class Argument(
            val colorArgb: Int,
            val expectedHue: Float,
            val expectedSaturation: Float,
            val expectedLightness: Float
        )

        private fun colorToHslProvider() = listOf(
            Argument(Color.Red.toArgb(), 0f, 1f, 0.5f),
            Argument(Color.Green.toArgb(), 120f, 1f, 0.5f),
            Argument(Color.Blue.toArgb(), 240f, 1f, 0.5f),
            Argument(Color.Black.toArgb(), 0f, 0f, 0f),
            Argument(Color.White.toArgb(), 0f, 0f, 1f),
            Argument(Color(0.5f, 0.5f, 0.5f).toArgb(), 0f, 0f, 0.5f),
            Argument(Color.Yellow.toArgb(), 60f, 1f, 0.5f),
            Argument(Color.Cyan.toArgb(), 180f, 1f, 0.5f),
            Argument(Color.Magenta.toArgb(), 300f, 1f, 0.5f)
        )
    }

    @Test
    fun test_toHsl() {
        colorToHslProvider().forEach { (colorArgb, expectedHue, expectedSaturation, expectedLightness) ->
            val hsl = Color(colorArgb).toHsl()

            assertThat(expectedHue, "hue").isCloseTo(hsl.hue, TOLERANCE)
            assertThat(expectedSaturation, "saturation").isCloseTo(hsl.saturation, TOLERANCE)
            assertThat(expectedLightness, "lightness").isCloseTo(hsl.lightness, TOLERANCE)
        }
    }
}
