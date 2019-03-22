package com.rud.fastjobs

import android.app.Application
import com.rud.fastjobs.data.repository.UserRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber

class MyApplication : Application(), KodeinAware {
    init {
        Timber.plant(Timber.DebugTree())
        Timber.d("Timber planted")
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { UserRepository() }
    }
}