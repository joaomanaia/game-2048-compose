package com.joaomanaia.game2048.ui.color_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.joaomanaia.game2048.core.navigation.Screen
import com.joaomanaia.game2048.core.ui.DarkThemeConfig
import com.joaomanaia.game2048.core.ui.Game2048Theme
import com.joaomanaia.game2048.core.ui.TileColorsGenerator
import com.joaomanaia.game2048.core.ui.spacing
import com.joaomanaia.game2048.ui.color_settings.components.BaseColorChooser
import com.joaomanaia.game2048.ui.color_settings.components.DarkThemeDialogPicker
import com.joaomanaia.game2048.ui.components.BackIconButton
import com.joaomanaia.game2048.ui.game.components.GameGridBackground
import com.joaomanaia.game2048.ui.game.components.grid.GridTileText
import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.math.roundToInt

@Serializable
object ColorSettingsScreenDestination : Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ColorSettingsScreen(
    navController: NavController,
    viewModel: ColorSettingsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ColorSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClicked = navController::navigateUp
    )
}

@Composable
@ExperimentalMaterial3Api
private fun ColorSettingsScreen(
    uiState: ColorSettingsUiState,
    onEvent: (ColorSettingsUiEvent) -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var darkThemeDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = "Color Settings") },
                navigationIcon = { BackIconButton(onClick = onBackClicked) },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item {
                BaseColorChooser(
                    onColorSelected = {}
                )
            }

            item {
                val isInDarkTheme = uiState.darkThemeConfig.shouldUseDarkTheme()

                val darkThemeText = if (isInDarkTheme) {
                    "Enabled"
                } else {
                    "Disabled"
                }

                ListItem(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .clickable(
                            onClick = { darkThemeDialogVisible = true },
                            role = Role.Button
                        ),
                    headlineContent = { Text(text = "Night Mode") },
                    supportingContent = { Text(text = darkThemeText) },
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                        ) {
                            VerticalDivider(modifier = Modifier.height(32.dp))
                            Switch(
                                checked = isInDarkTheme,
                                onCheckedChange = {
                                    val config = if (it) DarkThemeConfig.DARK else DarkThemeConfig.LIGHT

                                    onEvent(ColorSettingsUiEvent.OnDarkThemeChanged(config))
                                }
                            )
                        }
                    },
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(text = "Hue Increment") },
                    supportingContent = {
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            valueRange = 5f..20f,
                            steps = 10,
                            value = uiState.hueIncrement,
                            onValueChange = {}
                        )
                    },
                    trailingContent = {
                        Text(
                            text = uiState.hueIncrement.roundToInt().toString(),
                            modifier = Modifier.width(48.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }

            item {
                PreviewGrid(
                    baseColor = MaterialTheme.colorScheme.primary,
                    hueIncrement = uiState.hueIncrement
                )
            }
        }
    }

    if (darkThemeDialogVisible) {
        DarkThemeDialogPicker(
            darkThemeConfig = uiState.darkThemeConfig,
            onDarkThemeChanged = { onEvent(ColorSettingsUiEvent.OnDarkThemeChanged(it)) },
            onDismissRequest = { darkThemeDialogVisible = false }
        )
    }
}

@Composable
private fun PreviewGrid(
    modifier: Modifier = Modifier,
    baseColor: Color,
    hueIncrement: Float
) {
    val colors = remember(baseColor, hueIncrement) {
        TileColorsGenerator
            .generateHueSequence(
                baseColor = baseColor,
                hueIncrement = hueIncrement
            ).take(COLORS_TO_GENERATE)
    }

    GameGridBackground(
        modifier = modifier,
        gridSize = GRID_SIZE
    ) { tileSize ->
        val tileSizePx = with(LocalDensity.current) {
            tileSize.toPx()
        }

        val tileMarginPx = with(LocalDensity.current) { 4.dp.toPx() }
        val tileOffsetPx = tileSizePx + tileMarginPx

        colors.forEachIndexed { index, color ->
            // Calculates the tile number based in the index 2^n.
            val num = remember(index) {
                2f.pow(index + 1).roundToInt()
            }

            val offset = remember(index) {
                Offset(
                    x = (index % GRID_SIZE) * tileOffsetPx,
                    y = (index / GRID_SIZE) * tileOffsetPx
                )
            }

            GridTileText(
                modifier = Modifier.graphicsLayer(
                    translationX = offset.x,
                    translationY = offset.y
                ),
                num = num,
                size = tileSize,
                gridSize = GRID_SIZE,
                isPortrait = true,
                containerColor = color,
                contentColor = Color.White
            )
        }
    }
}

private const val GRID_SIZE = 4
private const val COLORS_TO_GENERATE = GRID_SIZE * GRID_SIZE

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun ColorSettingsScreenPreview() {
    Game2048Theme {
        ColorSettingsScreen(
            uiState = ColorSettingsUiState(
                hueIncrement = 15f
            )
        )
    }
}
