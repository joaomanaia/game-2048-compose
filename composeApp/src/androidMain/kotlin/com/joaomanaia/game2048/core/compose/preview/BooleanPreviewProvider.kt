package com.joaomanaia.game2048.core.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}
