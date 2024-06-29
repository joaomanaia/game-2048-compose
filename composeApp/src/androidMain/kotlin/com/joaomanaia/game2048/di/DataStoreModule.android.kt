package com.joaomanaia.game2048.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManager
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val Context.gameDataDataStore: DataStore<Preferences> by preferencesDataStore(name = "game_data")

actual val dataStoreModule = module {
    single { androidContext().gameDataDataStore }

    singleOf(::DataStoreManagerImpl) bind DataStoreManager::class
}
