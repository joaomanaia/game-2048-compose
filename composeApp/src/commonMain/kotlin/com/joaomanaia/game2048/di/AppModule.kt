package com.joaomanaia.game2048.di

import com.joaomanaia.game2048.presentation.MainViewModel
import com.joaomanaia.game2048.presentation.game.GameViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::GameViewModel)
}
