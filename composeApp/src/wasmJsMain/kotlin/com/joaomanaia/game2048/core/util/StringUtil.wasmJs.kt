package com.joaomanaia.game2048.core.util

actual fun formatSettingTrailingNumber(value: Float): String {
    val integerPart = value.toInt()
    val decimalPart = ((value - integerPart) * 100).toInt()

    return if (decimalPart < 10) {
        "$integerPart.0$decimalPart"
    } else {
        "$integerPart.$decimalPart"
    }
}
