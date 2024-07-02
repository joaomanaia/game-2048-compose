package com.joaomanaia.game2048.presentation.color_settings.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.joaomanaia.game2048.core.presentation.theme.Game2048Theme

@Composable
@PreviewLightDark
private fun BaseColorChooserPreview() {
    Game2048Theme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                var color by remember {
                    mutableStateOf(Color.Yellow)
                }

                BaseColorChooser(
                    useDarkTheme = isSystemInDarkTheme(),
                    currentSeedColor = color,
                    wallpaperColors = emptySet(),
                    defaultSelectedTab = PreviewTabButtonType.BASIC_COLOR,
                    onColorSelected = { color = it }
                )
            }
        }
    }
}
