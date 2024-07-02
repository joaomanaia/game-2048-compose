package com.joaomanaia.game2048.core.util

import kotlin.text.format

actual fun formatSettingTrailingNumber(value: Float): String {
    return "%.2f".format(value)
}
