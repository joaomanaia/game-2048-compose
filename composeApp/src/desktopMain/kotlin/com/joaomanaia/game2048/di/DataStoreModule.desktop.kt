package com.joaomanaia.game2048.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManager
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManagerImpl
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val gameDataStoreFileName = "game_data.preferences_pb"


actual val dataStoreModule = module {
    single { createDataStore { gameDataStoreFileName } }

    singleOf(::DataStoreManagerImpl) bind DataStoreManager::class
}
