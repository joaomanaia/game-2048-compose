package com.joaomanaia.game2048.di

import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object KoinStarter {
    private val logger = KotlinLogging.logger("KoinStarter")

    fun init(config: KoinAppDeclaration? = null) = startKoin {
        logger.trace { "Starting Koin" }
        config?.invoke(this)
        modules(appModule, dataStoreModule, repositoryModule, wallpaperColorsModule)
    }
}
