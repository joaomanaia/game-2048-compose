package com.joaomanaia.game2048.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.joaomanaia.game2048.core.common.annotation.NamedQualifierRuntime
import com.joaomanaia.game2048.core.common.preferences.gameDataDataStore
import com.joaomanaia.game2048.core.manager.DataStoreManager
import com.joaomanaia.game2048.core.manager.DataStoreManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@NamedQualifierRuntime
annotation class GameDataPreferences

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    @GameDataPreferences
    fun provideGameDataPreferencesDataStore(
        @ApplicationContext content: Context
    ): DataStore<Preferences> = content.gameDataDataStore

    @Provides
    @Singleton
    @GameDataPreferences
    fun provideGameDataDataStoreManager(
        @GameDataPreferences dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManagerImpl(dataStore)
}
