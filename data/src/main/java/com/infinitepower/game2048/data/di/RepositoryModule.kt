package com.infinitepower.game2048.data.di

import com.infinitepower.game2048.core.datastore.manager.DataStoreManager
import com.infinitepower.game2048.core.di.GameDataPreferences
import com.infinitepower.game2048.data.game.SaveGameRepositoryImpl
import com.infinitepower.game2048.domain.SaveGameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSaveGameRepository(
        @GameDataPreferences gameDataStoreManager: DataStoreManager
    ): SaveGameRepository = SaveGameRepositoryImpl(gameDataStoreManager)
}