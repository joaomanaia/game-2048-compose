package com.joaomanaia.game2048.presentation.game.components.grid

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaomanaia.game2048.core.presentation.theme.Game2048Theme
import com.joaomanaia.game2048.model.Tile

@Composable
@PreviewLightDark
private fun GridTitleTextPreview() {
    Game2048Theme {
        Surface {
            GridTileText(
                modifier = Modifier
                    .padding(16.dp)
                    .size(100.dp),
                tile = Tile(2),
                fromScale = 0f,
                fromOffset = Offset(0f, 0f),
                toOffset = Offset(0f, 0f),
                moveCount = 0,
                textStyle = TextStyle(
                    fontSize = 30.sp
                )
            )
        }
    }
}
