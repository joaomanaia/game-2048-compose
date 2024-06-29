package com.joaomanaia.game2048.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.input.key.KeyEvent

val LocalKeyEventHandlers = compositionLocalOf<MutableList<KeyEventHandler>> {
    error("LocalKeyEventHandlers is not provided")
}

typealias KeyEventHandler = (Int, KeyEvent) -> Boolean

@Composable
fun ListenKeyEvents(handler: KeyEventHandler) {
    val handlerState = rememberUpdatedState(handler)
    val eventHandlers = LocalKeyEventHandlers.current

    DisposableEffect(handlerState) {
        val localHandler: KeyEventHandler = { keyCode, event ->
            handlerState.value(keyCode, event)
        }

        eventHandlers.add(localHandler)

        onDispose {
            eventHandlers.remove(localHandler)
        }
    }
}
