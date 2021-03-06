package com.rud.fastjobs

import android.app.Application
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.db.JobDao
import com.rud.fastjobs.data.db.NotificationsDB
import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.network.ConnectivityInterceptor
import com.rud.fastjobs.data.network.NearbyPlacesApiService
import com.rud.fastjobs.data.network.NearbyPlacesDataSource
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.StorageUtil
import com.rud.fastjobs.data.repository.Store
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class MyApplication : Application(), KodeinAware {
    private lateinit var placesClient: PlacesClient

    init {
        Timber.plant(Timber.DebugTree())
        Timber.d("Timber planted")
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))
        val apiKey = getString(R.string.google_maps_key)

        bind() from singleton { NotificationsDB(instance()) }
        bind() from singleton { instance<NotificationsDB>().notificationDao() }
        bind() from singleton { FirebaseAuth.getInstance() }
        bind() from singleton { FirebaseFirestore.getInstance() }
        bind() from singleton { FirebaseStorage.getInstance() }
        bind() from singleton { StorageUtil(instance()) }
        bind() from singleton { UserDao(instance()) }
        bind() from singleton { JobDao(instance()) }
        bind() from singleton { Auth(instance(), instance()) }
        bind() from singleton { ConnectivityInterceptor(instance()) }
        bind() from singleton { NearbyPlacesApiService(apiKey, instance()) }
        bind() from singleton { NearbyPlacesDataSource(instance()) }
        bind() from singleton { MyRepository(instance(), instance(), instance(), instance(), instance()) }
        bind() from singleton { Store(instance()) }
        bind() from provider { ViewModelFactory(instance(), instance(), instance(), instance()) }
    }
}