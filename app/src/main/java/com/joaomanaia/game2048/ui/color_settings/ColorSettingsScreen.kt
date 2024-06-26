package com.joaomanaia.game2048.ui.color_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.HPlusMobiledata
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
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
import com.joaomanaia.game2048.ui.game.components.grid.GridContainer
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
                .fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.spacing.medium)
        ) {
            item {
                BaseColorChooser(
                    onColorSelected = {}
                )
            }

            item {
                NightModeSwitch(
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
                    darkThemeConfig = uiState.darkThemeConfig,
                    onEvent = onEvent
                )
            }

            item {
                PreviewGrid(
                    baseColor = MaterialTheme.colorScheme.primary,
                    hueParams = uiState.hueParams
                )
            }

            settingsGroupItem(title = "Tile Hue")

            item {
                ReverseHueSwitch(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    isIncrement = uiState.hueParams.isIncrement,
                    onChange = { onEvent(ColorSettingsUiEvent.OnIncrementHueChanged(it)) }
                )
            }

            item {
                SettingsItemSlider(
                    title = "Increment Value",
                    valueRange = HUE_INCREMENT_RANGE,
                    steps = HUE_INCREMENT_STEP,
                    value = uiState.hueParams.hueIncrement,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.HPlusMobiledata,
                            contentDescription = null
                        )
                    },
                    onValueChange = { onEvent(ColorSettingsUiEvent.OnHueIncrementChanged(it)) },
                    formatTrailingText = { it.toInt().toString() }
                )
            }

            item {
                SettingsItemSlider(
                    title = "Saturation",
                    valueRange = HUE_SAT_LIGHT_RANGE,
                    steps = HUE_SAT_LIGHT_STEP,
                    value = uiState.hueParams.saturation,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Contrast,
                            contentDescription = null
                        )
                    },
                    onValueChange = { onEvent(ColorSettingsUiEvent.OnHueSaturationChanged(it)) },
                )
            }

            item {
                SettingsItemSlider(
                    title = "Lightness",
                    valueRange = HUE_SAT_LIGHT_RANGE,
                    steps = HUE_SAT_LIGHT_STEP,
                    value = uiState.hueParams.lightness,
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.LightMode,
                            contentDescription = null
                        )
                    },
                    onValueChange = { onEvent(ColorSettingsUiEvent.OnHueLightnessChanged(it)) },
                )
            }
        }
    }
}

private fun LazyListScope.settingsGroupItem(
    modifier: Modifier = Modifier,
    title: String,
) {
    item {
        val spaceMedium = MaterialTheme.spacing.medium

        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            modifier = modifier
                .padding(start = spaceMedium, top = spaceMedium),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ReverseHueSwitch(
    modifier: Modifier = Modifier,
    isIncrement: Boolean,
    onChange: (Boolean) -> Unit
) {
    ListItem(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(
                onClick = { onChange(!isIncrement) },
                role = Role.Button
            ),
        headlineContent = { Text(text = "Reverse Hue") },
        supportingContent = {
            val text = if (isIncrement) {
                "The hue value will be incremented"
            } else {
                "The hue value will be decremented."
            }

            Text(text = text)
        },
        trailingContent = {
            Switch(
                checked = isIncrement,
                onCheckedChange = onChange
            )
        },
    )
}

@Composable
private fun SettingsItemSlider(
    modifier: Modifier = Modifier,
    title: String,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    value: Float,
    icon: @Composable (() -> Unit)? = null,
    onValueChange: (Float) -> Unit,
    formatTrailingText: (Float) -> String = { "%.2f".format(it) }
) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = title) },
        supportingContent = {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                valueRange = valueRange,
                steps = steps,
                value = value,
                onValueChange = onValueChange
            )
        },
        trailingContent = {
            Text(
                text = formatTrailingText(value),
                modifier = Modifier.width(48.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        },
        leadingContent = icon
    )
}

@Composable
private fun NightModeSwitch(
    modifier: Modifier = Modifier,
    darkThemeConfig: DarkThemeConfig,
    onEvent: (ColorSettingsUiEvent) -> Unit
) {
    val isInDarkTheme = darkThemeConfig.shouldUseDarkTheme()

    var darkThemeDialogVisible by remember { mutableStateOf(false) }

    val darkThemeText = if (isInDarkTheme) {
        "Enabled"
    } else {
        "Disabled"
    }

    ListItem(
        modifier = modifier
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

    if (darkThemeDialogVisible) {
        DarkThemeDialogPicker(
            darkThemeConfig = darkThemeConfig,
            onDarkThemeChanged = { onEvent(ColorSettingsUiEvent.OnDarkThemeChanged(it)) },
            onDismissRequest = { darkThemeDialogVisible = false }
        )
    }
}

@Composable
private fun PreviewGrid(
    modifier: Modifier = Modifier,
    baseColor: Color,
    hueParams: TileColorsGenerator.HueParams
) {
    GridContainer(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacing.medium)
            .clip(MaterialTheme.shapes.large),
        gridSize = GRID_SIZE
    ) { tileWidth, tileOffsetPx, textFontSize ->
        for (row in 0 until GRID_SIZE) {
            for (col in 0 until GRID_SIZE) {
                val logNum = remember(row, col) {
                    row * GRID_SIZE + col + 1
                }

                val num = remember(logNum) {
                    2.0.pow(logNum).roundToInt()
                }

                val containerColor = remember(logNum, baseColor, hueParams) {
                    TileColorsGenerator.getColorForTile(
                        logNum = logNum,
                        baseColor = baseColor,
                        hueParams = hueParams
                    )
                }

                val density = LocalDensity.current

                val offsetX = with(density) { (col * tileOffsetPx).toDp() }
                val offsetY = with(density) { (row * tileOffsetPx).toDp() }

                GridTileText(
                    modifier = Modifier
                        .size(tileWidth)
                        .offset(
                            x = offsetX,
                            y = offsetY
                        ),
                    num = num,
                    textStyle = TextStyle(fontSize = textFontSize),
                    containerColor = containerColor,
                    contentColor = Color.White
                )
            }

        }
    }
}

private const val GRID_SIZE = 4

// Hue increment slider settings
private const val MIN_HUE_INCREMENT = 5f
private const val MAX_HUE_INCREMENT = 20f
private val HUE_INCREMENT_RANGE = MIN_HUE_INCREMENT..MAX_HUE_INCREMENT
private const val HUE_INCREMENT_STEP = (MAX_HUE_INCREMENT - MIN_HUE_INCREMENT).toInt() - 1

// Hue saturation and lightness slider settings
private const val MIN_HUE_SAT_LIGHT = 0.2f
private const val MAX_HUE_SAT_LIGHT = 0.8f
private val HUE_SAT_LIGHT_RANGE = MIN_HUE_SAT_LIGHT..MAX_HUE_SAT_LIGHT
private const val HUE_SAT_LIGHT_STEP = 11

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun ColorSettingsScreenPreview() {
    Game2048Theme {
        ColorSettingsScreen(
            uiState = ColorSettingsUiState()
        )
    }
}
