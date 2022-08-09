package com.joaomanaia.game2048.core.ui

import androidx.annotation.Keep
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors

@Keep
data class CustomColor(
    val key: Keys,
    val color: Color,
    val harmonized: Boolean,
    val roles: ColorRoles
) {
    enum class Keys {
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
    data class ColorRoles(
        val accent: Color,
        val onAccent: Color,
        val accentContainer: Color,
        val onAccentContainer: Color
    )
}

private fun initializeColorRoles() = CustomColor.ColorRoles(
    accent = Color.Unspecified,
    onAccent = Color.Unspecified,
    accentContainer = Color.Unspecified,
    onAccentContainer = Color.Unspecified,
)

private fun ColorRoles.toColorRoles(): CustomColor.ColorRoles = CustomColor.ColorRoles(
    accent = Color(this.accent),
    onAccent = Color(this.onAccent),
    accentContainer = Color(this.accentContainer),
    onAccentContainer = Color(this.onAccentContainer),
)

@Keep
data class ExtendedColors(
    val colors: List<CustomColor>
) {
    @Composable
    @ReadOnlyComposable
    fun getColorRolesByKey(
        key: CustomColor.Keys
    ): CustomColor.ColorRoles {
        val color = colors.find { color -> color.key == key }

        return if (color != null && color.harmonized) {
            color.roles
        } else {
            CustomColor.ColorRoles(
                accent = color?.color ?: MaterialTheme.colorScheme.primary,
                onAccent = color?.color ?: MaterialTheme.colorScheme.onPrimary,
                accentContainer = color?.color ?: MaterialTheme.colorScheme.primaryContainer,
                onAccentContainer = color?.color ?: MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }

    @Composable
    @ReadOnlyComposable
    fun getColorAccentByKey(
        key: CustomColor.Keys
    ): Color = getColorRolesByKey(key = key).accent

    @Composable
    @ReadOnlyComposable
    fun getColorOnAccentByKey(
        key: CustomColor.Keys
    ): Color = getColorRolesByKey(key = key).onAccent
}

private val initializeExtend = ExtendedColors(
    listOf(
        CustomColor(
            key = CustomColor.Keys.Tile2,
            color = Color(238, 228, 218),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile4,
            color = Color(237, 224, 200),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile8,
            color = Color(242, 177, 121),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile16,
            color = Color(245, 149, 99),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile32,
            color = Color(246, 124, 95),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile64,
            color = Color(246, 94, 59),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile128,
            color = Color(237, 207, 114),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile256,
            color = Color(237, 204, 97),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile512,
            color = Color(237, 200, 80),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile1024,
            color = Color(237, 197, 63),
            harmonized = true,
            roles = initializeColorRoles()
        ),
        CustomColor(
            key = CustomColor.Keys.Tile2048,
            color = Color(237, 194, 63),
            harmonized = true,
            roles = initializeColorRoles()
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
        // Retrieve record
        val shouldHarmonize = customColor.harmonized
        // Blend or not
        if (shouldHarmonize) {
            val blendedColor = MaterialColors.harmonize(customColor.color.toArgb(), colorScheme.primary.toArgb())

            val roles = MaterialColors.getColorRoles(blendedColor, isLight)

            customColor.copy(roles = roles.toColorRoles())
        } else {
            val roles = MaterialColors.getColorRoles(customColor.color.toArgb(), isLight)
            customColor.copy(roles = roles.toColorRoles())
        }
    }
    return ExtendedColors(colors)
}