package com.joaomanaia.game2048.di

import com.joaomanaia.game2048.core.datastore.manager.DataStoreManager
import com.joaomanaia.game2048.core.datastore.manager.DataStoreManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val dataStoreModule = module {
    singleOf(::DataStoreManagerImpl) bind DataStoreManager::class
}