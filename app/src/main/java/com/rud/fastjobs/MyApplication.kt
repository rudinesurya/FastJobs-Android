package com.rud.fastjobs

import android.app.Application
import timber.log.Timber

class MyApplication : Application() {
    init {
        Timber.plant(Timber.DebugTree())
        Timber.d("Timber planted")
    }
}