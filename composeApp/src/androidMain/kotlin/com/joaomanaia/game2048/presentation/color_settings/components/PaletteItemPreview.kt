package com.joaomanaia.game2048.presentation.color_settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.joaomanaia.game2048.core.compose.preview.BooleanPreviewProvider
import com.joaomanaia.game2048.core.presentation.theme.Game2048Theme


@Composable
@PreviewLightDark
private fun PaletteItemPreview(
    @PreviewParameter(BooleanPreviewProvider::class) selected: Boolean
) {
    Game2048Theme {
        Surface {
            SelectablePaletteItem(
                modifier = Modifier
                    .padding(8.dp)
                    .size(75.dp),
                selected = selected,
                baseColor = Color.Blue,
                onClick = {}
            )
        }
    }
}
