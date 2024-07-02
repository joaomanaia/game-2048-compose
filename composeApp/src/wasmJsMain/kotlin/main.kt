import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.window.CanvasBasedWindow
import com.joaomanaia.game2048.core.compose.KeyEventHandler
import com.joaomanaia.game2048.core.compose.LocalKeyEventHandlers
import com.joaomanaia.game2048.di.KoinStarter
import com.joaomanaia.game2048.presentation.App
import com.joaomanaia.game2048.presentation.MainUiState
import com.joaomanaia.game2048.presentation.MainViewModel
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.KotlinLoggingConfiguration
import io.github.oshai.kotlinlogging.Level
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

private val logger = KotlinLogging.logger("WasmJsMain")

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
    InternalComposeUiApi::class
)
fun main() {
    KotlinLoggingConfiguration.logLevel = Level.DEBUG
    KoinStarter.init()

    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        val mainViewModel: MainViewModel = koinInject()

        var uiState: MainUiState by remember { mutableStateOf(MainUiState.Loading) }

//    with(LocalLifecycleOwner.current) {
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mainViewModel.uiState
//                    .onEach { uiState = it }
//                    .collect()
//            }
//        }
//    }

        val scope = rememberCoroutineScope()
        scope.launch {
            mainViewModel.uiState.collect { uiState = it }
        }

        val keyEventHandlers = remember { mutableStateListOf<KeyEventHandler>() }
        DetectKeyEvents { nativeEvent ->
            val keyEvent = KeyEvent(
                key = Key(nativeEvent.keyCode.toLong()),
                type = KeyEventType.KeyUp,
            )

            keyEventHandlers.reversed().any {
                it(nativeEvent.keyCode, keyEvent)
            }
        }

//        val windowSizeClass = WindowSizeClass.calculateFromSize(windowState.size)
        val windowSizeClass = calculateWindowSizeClass()

        CompositionLocalProvider(
            LocalKeyEventHandlers provides keyEventHandlers
        ) {
            App(
                windowSizeClass = windowSizeClass,
                uiState = uiState
            )
        }
    }
}

@Composable
private fun DetectKeyEvents(
    onKeyEvent: (nativeEvent: KeyboardEvent) -> Boolean
) {
    DisposableEffect(Unit) {
        val handleKeyDown: (Event) -> Unit = { event: Event ->
            if (event is KeyboardEvent) {
                logger.debug { "Keyboard event, key: ${event.key}" }
                onKeyEvent(event)
            }
        }

        window.addEventListener("keyup", handleKeyDown)

        onDispose { window.removeEventListener("keyup", handleKeyDown) }
    }
}
