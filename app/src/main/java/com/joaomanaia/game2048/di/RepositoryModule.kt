package com.joaomanaia.game2048.di

import com.joaomanaia.game2048.data.repository.SaveGameRepositoryImpl
import com.joaomanaia.game2048.domain.repository.SaveGameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideSaveGameRepository(saveGameRepositoryImpl: SaveGameRepositoryImpl): SaveGameRepository
}