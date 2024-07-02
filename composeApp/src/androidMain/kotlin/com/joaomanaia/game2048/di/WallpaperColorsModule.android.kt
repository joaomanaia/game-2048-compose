package com.joaomanaia.game2048.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.joaomanaia.game2048.core.presentation.theme.WallpaperColors

actual val wallpaperColorsModule = module {
    singleOf(::WallpaperColors)
}
