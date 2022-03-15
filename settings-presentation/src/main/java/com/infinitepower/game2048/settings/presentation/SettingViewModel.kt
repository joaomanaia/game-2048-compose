package com.infinitepower.game2048.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.game2048.core.datastore.manager.DataStoreManager
import com.infinitepower.game2048.domain.SaveGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    private val saveGameRepository: SaveGameRepository
) : ViewModel() {
    fun onEvent(event: SettingsScreenUiEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is SettingsScreenUiEvent.OnChangeGridClick -> clearGrid()
        }
    }

    private suspend fun clearGrid() {
        saveGameRepository.saveGame(
            grid = emptyList(),
            currentScore = 0
        )
    }
}