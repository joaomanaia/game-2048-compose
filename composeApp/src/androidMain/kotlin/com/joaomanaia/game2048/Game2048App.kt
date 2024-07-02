package com.joaomanaia.game2048

import android.app.Application
import com.joaomanaia.game2048.di.KoinStarter
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import com.google.android.material.math.MathUtils.floorMod

class Game2048App : Application() {
    override fun onCreate() {
        super.onCreate()

        KoinStarter.init {
            androidLogger()
            androidContext(this@Game2048App)
        }
    }
}
