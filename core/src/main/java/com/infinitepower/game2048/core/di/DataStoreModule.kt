package com.infinitepower.game2048.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.infinitepower.game2048.core.common.annotation.NamedQualifierRuntime
import com.infinitepower.game2048.core.datastore.manager.DataStoreManager
import com.infinitepower.game2048.core.datastore.manager.DataStoreManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

const val GAME_DATA_PREFERENCES_NAME = "game_data_preferences"

@NamedQualifierRuntime
annotation class GameDataPreferences

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    @GameDataPreferences
    fun provideGameDataPreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        migrations = emptyList(),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = {
            appContext.preferencesDataStoreFile(GAME_DATA_PREFERENCES_NAME)
        }
    )

    @Provides
    @Singleton
    @GameDataPreferences
    fun provideGameDataDataStoreManager(
        @GameDataPreferences dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManagerImpl(dataStore)
}