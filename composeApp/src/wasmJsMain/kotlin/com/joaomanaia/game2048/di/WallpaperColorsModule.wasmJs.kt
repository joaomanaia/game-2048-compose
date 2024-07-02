package com.joaomanaia.game2048.di

import com.joaomanaia.game2048.core.presentation.theme.WallpaperColors
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val wallpaperColorsModule = module {
    singleOf(::WallpaperColors)
}
