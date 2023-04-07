package com.petrs.smartlab.app

import android.app.Application
import com.petrs.smartlab.BuildConfig
import com.petrs.smartlab.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            modules(
                domainModule,
                repositoryModule,
                sharedPreferencesModule,
                uiModule
            )
        }
    }
}