package com.joaomanaia.game2048.core.presentation.theme

import androidx.annotation.Keep
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Keep
@Immutable
data class Spacing(
    /** 8.dp **/
    val default: Dp = 8.dp,
    /** 2.dp **/
    val tiny: Dp = 2.dp,
    /** 4.dp **/
    val extraSmall: Dp = 4.dp,
    /** 8.dp **/
    val small: Dp = 8.dp,
    /** 16.dp **/
    val medium: Dp = 16.dp,
    /** 32.dp **/
    val large: Dp = 32.dp,
    /** 64.dp **/
    val extraLarge: Dp = 64.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
