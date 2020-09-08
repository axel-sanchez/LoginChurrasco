package com.example.loginchurrasco.aplication

import android.app.Application
import com.example.loginchurrasco.di.moduleApp
import org.koin.android.ext.android.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(moduleApp))
    }
}