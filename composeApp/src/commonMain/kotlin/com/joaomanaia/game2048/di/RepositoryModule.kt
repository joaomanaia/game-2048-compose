package com.joaomanaia.game2048.di

import com.joaomanaia.game2048.data.repository.SaveGameRepositoryImpl
import com.joaomanaia.game2048.domain.repository.SaveGameRepository
import com.joaomanaia.game2048.domain.usecase.GetHueParamsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::SaveGameRepositoryImpl) bind SaveGameRepository::class

    singleOf(::GetHueParamsUseCase)
}
