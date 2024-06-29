package com.joaomanaia.game2048.presentation.game

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Grid4x4
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import com.joaomanaia.game2048.core.navigation.Screen
import com.joaomanaia.game2048.core.presentation.theme.spacing
import com.joaomanaia.game2048.model.Direction
import com.joaomanaia.game2048.presentation.components.GameDialog
import com.joaomanaia.game2048.presentation.game.components.grid.ChangeGameGridDialog
import com.joaomanaia.game2048.presentation.game.components.grid.GameGrid
import game2048.composeapp.generated.resources.Res
import game2048.composeapp.generated.resources.app_name
import game2048.composeapp.generated.resources.best
import game2048.composeapp.generated.resources.game_over
import game2048.composeapp.generated.resources.grid_size
import game2048.composeapp.generated.resources.reset_game
import game2048.composeapp.generated.resources.score
import game2048.composeapp.generated.resources.settings
import game2048.composeapp.generated.resources.start_new_game_q
import game2048.composeapp.generated.resources.start_new_game_warning
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.math.sqrt

@Composable
@OptIn(KoinExperimentalAPI::class)
internal fun GameScreen(
    navController: NavController,
    gameViewModel: GameViewModel = koinViewModel()
) {
    val homeScreenUiState by gameViewModel.homeScreenUiState.collectAsState()

    GameScreenContent(
        navController = navController,
        uiState = homeScreenUiState,
        onSwipeListener = { direction ->
            gameViewModel.onEvent(GameScreenUiEvent.OnMoveGrid(direction))
        },
        onNewGameRequested = {
            gameViewModel.onEvent(GameScreenUiEvent.OnStartNewGameRequest)
        },
        onGridSizeChange = { newSize ->
            gameViewModel.onEvent(GameScreenUiEvent.OnGridSizeChange(newSize))
        },
    )
}

/**
 * 2040 game home screen content
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun GameScreenContent(
    navController: NavController,
    uiState: GameScreenUiState,
    onNewGameRequested: () -> Unit,
    onSwipeListener: (direction: Direction) -> Unit,
    onGridSizeChange: (newSize: Int) -> Unit,
) {
    val (resetDialogVisible, setResetDialogVisible) = remember {
        mutableStateOf(false)
    }
    val (optionsVisible, setOptionsVisible) = remember {
        mutableStateOf(false)
    }
    val (changeGridDialogVisible, setChangeGridDialogVisible) = remember {
        mutableStateOf(false)
    }

    val currentScoreAnimated by animateIntAsState(
        targetValue = uiState.currentScore,
        label = "Current Score"
    )
    val bestScoreAnimated by animateIntAsState(
        targetValue = uiState.bestScore,
        label = "Best Score"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(Res.string.app_name))
                },
                actions = {
                    IconButton(onClick = { setResetDialogVisible(true) }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(Res.string.reset_game)
                        )
                    }
                    IconButton(onClick = { setOptionsVisible(true) }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(Res.string.settings)
                        )
                    }

                    DropdownMenu(
                        expanded = optionsVisible,
                        onDismissRequest = { setOptionsVisible(false) }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(Res.string.grid_size)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Grid4x4,
                                    contentDescription = stringResource(Res.string.grid_size)
                                )
                            },
                            onClick = {
                                setOptionsVisible(false)
                                setChangeGridDialogVisible(true)
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(text = "Color Settings") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Palette,
                                    contentDescription = "Color Settings"
                                )
                            },
                            onClick = {
                                navController.navigate(Screen.COLOR_SETTINGS.name)
                                setOptionsVisible(false)
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val isPortrait = true // TODO

        val localViewConfiguration = LocalViewConfiguration.current
        var totalDragDistance: Offset = Offset.Zero

        val spaceMedium = MaterialTheme.spacing.medium

        val constraints = remember(isPortrait) {
            buildConstraints(isPortrait = isPortrait, spaceMedium = spaceMedium)
        }

        ConstraintLayout(
            constraintSet = constraints,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            val (dx, dy) = totalDragDistance
                            val swipeDistance = dist(dx, dy)
                            if (swipeDistance < localViewConfiguration.touchSlop)
                                return@detectDragGestures

                            val swipeAngle = atan2(dx, -dy)
                            onSwipeListener(
                                when {
                                    45 <= swipeAngle && swipeAngle < 135 -> Direction.UP
                                    135 <= swipeAngle && swipeAngle < 225 -> Direction.LEFT
                                    225 <= swipeAngle && swipeAngle < 315 -> Direction.DOWN
                                    else -> Direction.RIGHT
                                }
                            )
                        },
                        onDragStart = {
                            totalDragDistance = Offset.Zero
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        totalDragDistance += dragAmount
                    }
                }
        ) {
            TextLabel(
                text = currentScoreAnimated.toString(),
                layoutId = LayoutRef.CURRENT_SCORE_TEXT,
                style = MaterialTheme.typography.headlineMedium
            )
            TextLabel(
                text = stringResource(Res.string.score),
                layoutId = LayoutRef.CURRENT_SCORE_LABEL,
                style = MaterialTheme.typography.titleMedium
            )
            TextLabel(
                text = bestScoreAnimated.toString(),
                layoutId = LayoutRef.BEST_SCORE_TEXT,
                style = MaterialTheme.typography.headlineMedium
            )
            TextLabel(
                text = stringResource(Res.string.best),
                layoutId = LayoutRef.BEST_SCORE_LABEL,
                style = MaterialTheme.typography.titleMedium
            )
            GameGrid(
                gridTileMovements = uiState.gridTileMovements,
                moveCount = uiState.moveCount,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(MaterialTheme.spacing.medium)
                    .layoutId(LayoutRef.GAME_GRID),
                gridSize = uiState.gridSize,
                hueParams = uiState.hueParams
            )
        }

        when {
            uiState.isGameOver -> GameDialog(
                title = stringResource(Res.string.game_over),
                message = stringResource(Res.string.start_new_game_q),
                onConfirmListener = onNewGameRequested,
                onDismissListener = { setResetDialogVisible(false) }
            )

            resetDialogVisible -> GameDialog(
                title = stringResource(Res.string.start_new_game_q),
                message = stringResource(Res.string.start_new_game_warning),
                onConfirmListener = {
                    onNewGameRequested()
                    setResetDialogVisible(false)
                },
                onDismissListener = { setResetDialogVisible(false) }
            )

            changeGridDialogVisible -> ChangeGameGridDialog(
                currentSize = uiState.gridSize,
                onDismissRequest = { setChangeGridDialogVisible(false) },
                onGridSizeChange = { newSize ->
                    setChangeGridDialogVisible(false)
                    onGridSizeChange(newSize)
                }
            )
        }
    }
}

@Composable
private fun TextLabel(
    text: String,
    layoutId: String,
    style: TextStyle
) {
    Text(
        text = text,
        modifier = Modifier.layoutId(layoutId),
        style = style
    )
}

private fun dist(x: Float, y: Float): Float = sqrt(x * y + y * y)

private fun atan2(x: Float, y: Float): Float {
    var degrees = Math.toDegrees(kotlin.math.atan2(y, x).toDouble()).toFloat()
    if (degrees < 0) {
        degrees += 360
    }
    return degrees
}

private fun buildConstraints(
    isPortrait: Boolean,
    spaceMedium: Dp
): ConstraintSet {
    return ConstraintSet {
        val gameGrid = createRefFor(LayoutRef.GAME_GRID)
        val currentScoreText = createRefFor(LayoutRef.CURRENT_SCORE_TEXT)
        val currentScoreLabel = createRefFor(LayoutRef.CURRENT_SCORE_LABEL)
        val bestScoreText = createRefFor(LayoutRef.BEST_SCORE_TEXT)
        val bestScoreLabel = createRefFor(LayoutRef.BEST_SCORE_LABEL)

        if (isPortrait) {
            constrain(gameGrid) {
                start.linkTo(parent.start)
                top.linkTo(currentScoreLabel.bottom, spaceMedium)
                end.linkTo(parent.end)
            }
            constrain(currentScoreText) {
                start.linkTo(parent.start, spaceMedium)
                top.linkTo(parent.top, spaceMedium)
            }
            constrain(currentScoreLabel) {
                start.linkTo(currentScoreText.start)
                top.linkTo(currentScoreText.bottom)
            }
            constrain(bestScoreText) {
                end.linkTo(gameGrid.end, spaceMedium)
                top.linkTo(parent.top, spaceMedium)
            }
            constrain(bestScoreLabel) {
                end.linkTo(bestScoreText.end)
                top.linkTo(bestScoreText.bottom)
            }
        } else {
            constrain(gameGrid) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            constrain(currentScoreText) {
                start.linkTo(currentScoreLabel.start)
                top.linkTo(gameGrid.top, spaceMedium)
            }
            constrain(currentScoreLabel) {
                start.linkTo(bestScoreText.start)
                top.linkTo(currentScoreText.bottom)
            }
            constrain(bestScoreText) {
                start.linkTo(bestScoreLabel.start)
                top.linkTo(currentScoreLabel.bottom, spaceMedium)
            }
            constrain(bestScoreLabel) {
                start.linkTo(gameGrid.end)
                top.linkTo(bestScoreText.bottom)
            }
            createHorizontalChain(gameGrid, bestScoreLabel, chainStyle = ChainStyle.Packed)
        }
    }
}

private object LayoutRef {
    const val GAME_GRID = "gameGrid"
    const val CURRENT_SCORE_TEXT = "currentScoreText"
    const val CURRENT_SCORE_LABEL = "currentScoreLabel"
    const val BEST_SCORE_TEXT = "bestScoreText"
    const val BEST_SCORE_LABEL = "bestScoreLabel"
}
