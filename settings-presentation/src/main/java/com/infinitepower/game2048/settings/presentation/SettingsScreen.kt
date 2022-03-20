package com.infinitepower.game2048.settings.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.infinitepower.game2048.core.common.preferences.GameDataPreferencesCommon
import com.infinitepower.game2048.core.datastore.manager.DataStoreManager
import com.infinitepower.game2048.core.datastore.manager.settings.SettingsPreference
import com.infinitepower.game2048.core.ui.spacing
import com.infinitepower.game2048.settings.presentation.components.SettingsPreferenceItem
import com.infinitepower.game2048.settings.presentation.components.icons.Grid4x4

@Composable
fun SettingsScreen(
    settingsViewModel: SettingViewModel = hiltViewModel(),
    navController: NavHostController
) {
    SettingsScreenContent(
        dataStoreManager = settingsViewModel.dataStoreManager,
        onBackClick = navController::popBackStack,
        onUiEvent = settingsViewModel::onEvent
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingsScreenContent(
    dataStoreManager: DataStoreManager,
    onBackClick: () -> Unit,
    onUiEvent: (event: SettingsScreenUiEvent) -> Unit,
) {
    val gridSizeOptions = mapOf(
        "3" to com.infinitepower.game2048.core.R.string.grid_size_3,
        "4" to com.infinitepower.game2048.core.R.string.grid_size_4,
        "5" to com.infinitepower.game2048.core.R.string.grid_size_5,
        "6" to com.infinitepower.game2048.core.R.string.grid_size_6,
        "7" to com.infinitepower.game2048.core.R.string.grid_size_7,
    )
    
    val settingsList = listOf(
        SettingsPreference.PreferenceItem.ListPreference(
            request = GameDataPreferencesCommon.GridSize,
            title = stringResource(id = com.infinitepower.game2048.core.R.string.grid_size),
            summary = stringResource(id = com.infinitepower.game2048.core.R.string.grid_size_summary),
            singleLineTitle = true,
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Grid4x4,
                    contentDescription = stringResource(id = com.infinitepower.game2048.core.R.string.grid_size),
                    modifier = Modifier.padding(MaterialTheme.spacing.small)
                )
            },
            entries = gridSizeOptions,
            onUpdateValue = { onUiEvent(SettingsScreenUiEvent.OnChangeGridClick) }
        )
    )

    val prefs by dataStoreManager.preferenceFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = com.infinitepower.game2048.core.R.string.settings))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = com.infinitepower.game2048.core.R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(
                items = settingsList,
                key = { it.request.key.name }
            ) { settingItem ->
                SettingsPreferenceItem(
                    item = settingItem,
                    prefs = prefs,
                    dataStoreManager = dataStoreManager
                )
            }
        }
    }
}