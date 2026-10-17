package com.example.spendly

import android.app.Application

class SpendlyApp: Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}