package com.rud.fastjobs

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rud.fastjobs.data.db.JobDao
import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.viewmodel.AccountViewModelFactory
import com.rud.fastjobs.viewmodel.JobListViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber


class MyApplication : Application(), KodeinAware {
    init {
        Timber.plant(Timber.DebugTree())
        Timber.d("Timber planted")
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { FirebaseAuth.getInstance() }
        bind() from singleton { FirebaseFirestore.getInstance() }
        bind() from singleton { FirebaseStorage.getInstance() }
        bind() from singleton { UserDao(instance(), instance(), instance()) }
        bind() from singleton { JobDao(instance()) }
        bind() from singleton { MyRepository(instance(), instance()) }
        bind() from provider { AccountViewModelFactory(instance()) }
        bind() from provider { JobListViewModelFactory(instance()) }
    }
}