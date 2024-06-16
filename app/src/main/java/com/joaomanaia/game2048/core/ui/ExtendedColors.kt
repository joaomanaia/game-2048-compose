package com.joaomanaia.game2048.core.ui

import androidx.annotation.Keep
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors

@Keep
@Immutable
data class CustomColor(
    val key: Key,
    val originalColor: Color,
    val harmonize: Boolean = true,
    val roles: ColorRoles = unspecifiedColorRoles
) {
    enum class Key {
        Tile2,
        Tile4,
        Tile8,
        Tile16,
        Tile32,
        Tile64,
        Tile128,
        Tile256,
        Tile512,
        Tile1024,
        Tile2048,
    }

    @Keep
    @Immutable
    data class ColorRoles(
        val color: Color,
        val onColor: Color,
        val colorContainer: Color,
        val onColorContainer: Color
    )
}

private val unspecifiedColorRoles = CustomColor.ColorRoles(
    color = Color.Unspecified,
    onColor = Color.Unspecified,
    colorContainer = Color.Unspecified,
    onColorContainer = Color.Unspecified,
)

private fun ColorRoles.toColorRoles(): CustomColor.ColorRoles = CustomColor.ColorRoles(
    color = Color(this.accent),
    onColor = Color(this.onAccent),
    colorContainer = Color(this.accentContainer),
    onColorContainer = Color(this.onAccentContainer),
)

@Keep
@Immutable
data class ExtendedColors(
    val colors: List<CustomColor>
) {
    fun getColorsByKey(key: CustomColor.Key): CustomColor.ColorRoles {
        return colors.find { color -> color.key == key }?.roles
            ?: error("No color found for key $key")
    }

    fun getColorByKey(key: CustomColor.Key): Color = getColorsByKey(key).color
    fun getOnColorByKey(key: CustomColor.Key): Color = getColorsByKey(key).onColor
    fun getColorContainerByKey(key: CustomColor.Key): Color = getColorsByKey(key).colorContainer
    fun getOnColorContainerByKey(key: CustomColor.Key): Color = getColorsByKey(key).onColorContainer
}

private val initializeExtend = ExtendedColors(
    listOf(
        CustomColor(
            key = CustomColor.Key.Tile2,
            originalColor = Color(238, 228, 218),
        ),
        CustomColor(
            key = CustomColor.Key.Tile4,
            originalColor = Color(237, 224, 200),
        ),
        CustomColor(
            key = CustomColor.Key.Tile8,
            originalColor = Color(242, 177, 121),
        ),
        CustomColor(
            key = CustomColor.Key.Tile16,
            originalColor = Color(245, 149, 99),
        ),
        CustomColor(
            key = CustomColor.Key.Tile32,
            originalColor = Color(246, 124, 95),
        ),
        CustomColor(
            key = CustomColor.Key.Tile64,
            originalColor = Color(246, 94, 59),
        ),
        CustomColor(
            key = CustomColor.Key.Tile128,
            originalColor = Color(237, 207, 114),
        ),
        CustomColor(
            key = CustomColor.Key.Tile256,
            originalColor = Color(237, 204, 97),
        ),
        CustomColor(
            key = CustomColor.Key.Tile512,
            originalColor = Color(237, 200, 80),
        ),
        CustomColor(
            key = CustomColor.Key.Tile1024,
            originalColor = Color(237, 197, 63),
        ),
        CustomColor(
            key = CustomColor.Key.Tile2048,
            originalColor = Color(237, 194, 63),
        ),
    )
)

val LocalExtendedColors = staticCompositionLocalOf {
    initializeExtend
}

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current


internal fun setupCustomColors(
    colorScheme: ColorScheme,
    isLight: Boolean
): ExtendedColors {
    val colors = initializeExtend.colors.map { customColor ->
        val shouldHarmonize = customColor.harmonize

        // Harmonize the color if needed, if not, use the original color to get the roles
        val color = if (shouldHarmonize) {
            MaterialColors.harmonize(
                customColor.originalColor.toArgb(),
                colorScheme.primary.toArgb()
            )
        } else {
            customColor.originalColor.toArgb()
        }

        val roles = MaterialColors.getColorRoles(color, isLight)
        customColor.copy(roles = roles.toColorRoles())
    }

    return ExtendedColors(colors)
}
