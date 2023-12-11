package com.example.bloodcardapp

import android.app.Application
import com.example.bloodcardapp.di.repositoryModule
import com.example.bloodcardapp.di.viewModelModule
import com.google.firebase.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BloodCardApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(
                if(BuildConfig.DEBUG)
                    Level.ERROR
                else
                    Level.NONE
            )
            androidContext(this@BloodCardApplication)
            modules(
                repositoryModule,
                viewModelModule
            )
        }
    }
}