package com.joaomanaia.game2048.domain.usecase

import com.joaomanaia.game2048.core.common.preferences.GameDataPreferencesCommon
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManager
import com.joaomanaia.game2048.core.presentation.theme.TileColorsGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetHueParamsUseCase(
    private val gameDataStoreManager: DataStoreManager
) {
    operator fun invoke(): Flow<TileColorsGenerator.HueParams> = combine(
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.IncrementHue),
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.HueIncrementValue),
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.HueSaturation),
        gameDataStoreManager.getPreferenceFlow(GameDataPreferencesCommon.HueLightness)
    ) { incrementHue, hueIncrement, hueSaturation, hueLightness ->
        TileColorsGenerator.HueParams(
            isIncrement = incrementHue,
            hueIncrement = hueIncrement,
            saturation = hueSaturation,
            lightness = hueLightness
        )
    }
}
