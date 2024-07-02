package com.joaomanaia.game2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.KeyEvent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.joaomanaia.game2048.core.compose.KeyEventHandler
import com.joaomanaia.game2048.core.compose.LocalKeyEventHandlers
import com.joaomanaia.game2048.presentation.App
import com.joaomanaia.game2048.presentation.MainUiState
import com.joaomanaia.game2048.presentation.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    private val keyEventHandlers = mutableListOf<KeyEventHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainUiState by mutableStateOf(MainUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainUiState.Loading -> true
                is MainUiState.Success -> false
            }
        }

        setContent {
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

    override fun onKeyUp(keyCode: Int, event: android.view.KeyEvent): Boolean {
        return keyEventHandlers.reversed().any {
            it(keyCode, KeyEvent(event))
        } || super.onKeyUp(keyCode, event)
    }
}
