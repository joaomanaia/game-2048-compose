package com.infinitepower.game2048.home.presentation

import android.content.res.Configuration
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.infinitepower.game2048.core.common.GameCommon.NUM_INITIAL_TILES
import com.infinitepower.game2048.core.navigation.Screen
import com.infinitepower.game2048.core.ui.Game2048Theme
import com.infinitepower.game2048.core.ui.components.GameDialog
import com.infinitepower.game2048.core.ui.spacing
import com.infinitepower.game2048.core.util.createRandomAddedTile
import com.infinitepower.game2048.core.util.emptyGrid
import com.infinitepower.game2048.home.presentation.components.GameGrid
import com.infinitepower.game2048.model.Direction
import com.infinitepower.game2048.model.GridTileMovement
import kotlin.math.sqrt

/**
 * 2040 game home screen
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val homeScreenUiState by homeViewModel.homeScreenUiState.collectAsState()
    val gridSize by homeViewModel.gridSize.collectAsState()

    HomeScreenContent(
        gridTileMovements = homeScreenUiState.gridTileMovements,
        onSwipeListener = { direction ->
            homeViewModel.onEvent(HomeScreenUiEvent.OnMoveGrid(direction))
        },
        currentScore = homeScreenUiState.currentScore,
        bestScore = homeScreenUiState.bestScore,
        moveCount = homeScreenUiState.moveCount,
        isGameOver = homeScreenUiState.isGameOver,
        onNewGameRequested = {
            homeViewModel.onEvent(HomeScreenUiEvent.OnStartNewGameRequest)
        },
        navigateToSettings = {
            navController.navigate(Screen.SettingsScreen.route)
        },
        gridSize = gridSize
    )
}

/**
 * 2040 game home screen content
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeScreenContent(
    gridTileMovements: List<GridTileMovement>,
    currentScore: Int,
    bestScore: Int,
    moveCount: Int,
    isGameOver: Boolean,
    gridSize: Int,
    onNewGameRequested: () -> Unit,
    onSwipeListener: (direction: Direction) -> Unit,
    navigateToSettings: () -> Unit,
) {
    var resetGameDialog by remember {
        mutableStateOf(false)
    }
    
    val currentScoreAnimated by animateIntAsState(targetValue = currentScore)
    val bestScoreAnimated by animateIntAsState(targetValue = bestScore)

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(id = com.infinitepower.game2048.core.R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { resetGameDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Reset Game"
                        )
                    }
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) {
        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        val localViewConfiguration = LocalViewConfiguration.current
        var totalDragDistance: Offset = Offset.Zero

        ConstraintLayout(
            constraintSet = buildConstraints(isPortrait),
            modifier = Modifier
                .fillMaxSize()
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
                                    45 <= swipeAngle && swipeAngle < 135 -> Direction.Up
                                    135 <= swipeAngle && swipeAngle < 225 -> Direction.Left
                                    225 <= swipeAngle && swipeAngle < 315 -> Direction.Down
                                    else -> Direction.Right
                                }
                            )
                        },
                        onDragStart = {
                            totalDragDistance = Offset.Zero
                        }
                    ) { change, dragAmount ->
                        change.consumeAllChanges()
                        totalDragDistance += dragAmount
                    }
                }
        ) {
            TextLabel(
                text = "$currentScoreAnimated",
                layoutId = "currentScoreText",
                style = MaterialTheme.typography.headlineMedium
            )
            TextLabel(
                text = "Score",
                layoutId = "currentScoreLabel",
                style = MaterialTheme.typography.titleMedium
            )
            TextLabel(
                text = "$bestScoreAnimated",
                layoutId = "bestScoreText",
                style = MaterialTheme.typography.headlineMedium
            )
            TextLabel(
                text = "Best",
                layoutId = "bestScoreLabel",
                style = MaterialTheme.typography.titleMedium
            )
            GameGrid(
                gridTileMovements = gridTileMovements,
                moveCount = moveCount,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(MaterialTheme.spacing.medium)
                    .layoutId("gameGrid"),
                gridSize = gridSize
            )
        }

        if (isGameOver) {
            GameDialog(
                title = "Game Over",
                message = "Start a new game?",
                onConfirmListener = onNewGameRequested,
                onDismissListener = { resetGameDialog = false }
            )
        } else if (resetGameDialog) {
            GameDialog(
                title = "Start a new game?",
                message = "Starting a new game will erase your current game",
                onConfirmListener = {
                    onNewGameRequested()
                    resetGameDialog = false
                },
                onDismissListener = { resetGameDialog = false }
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

@Composable
@ReadOnlyComposable
private fun buildConstraints(isPortrait: Boolean): ConstraintSet {
    val spaceMedium = MaterialTheme.spacing.medium

    return ConstraintSet {
        val gameGrid = createRefFor("gameGrid")
        val currentScoreText = createRefFor("currentScoreText")
        val currentScoreLabel = createRefFor("currentScoreLabel")
        val bestScoreText = createRefFor("bestScoreText")
        val bestScoreLabel = createRefFor("bestScoreLabel")

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

@Composable
@Preview(showBackground = true)
private fun HomeScreenContentPrev() {
    val newGridTileMovements = (0 until NUM_INITIAL_TILES).mapNotNull {
        createRandomAddedTile(emptyGrid(4))
    }

    Game2048Theme {
        HomeScreenContent(
            gridTileMovements = newGridTileMovements,
            currentScore = 0,
            bestScore = 0,
            moveCount = 0,
            isGameOver = false,
            onNewGameRequested = {},
            onSwipeListener = {},
            navigateToSettings = {},
            gridSize = 4
        )
    }
}