import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.joaomanaia.game2048.di.KoinStarter
import com.joaomanaia.game2048.presentation.App
import com.joaomanaia.game2048.presentation.MainUiState
import com.joaomanaia.game2048.presentation.MainViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

fun main() = application {
    KoinStarter.init()

    val mainViewModel: MainViewModel = koinInject()

    var uiState: MainUiState by mutableStateOf(MainUiState.Loading)

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

    Window(onCloseRequest = ::exitApplication) {
        App(uiState)
    }
}
