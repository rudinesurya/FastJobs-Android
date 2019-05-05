package com.rud.fastjobs.viewmodel.jobDashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.libraries.places.api.Places
import com.rud.fastjobs.MyApplication
import com.rud.fastjobs.R
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.Store

class JobDashboardActivityViewModel(
    private val myRepository: MyRepository,
    private val store: Store,
    private val auth: Auth,
    app: Application
) : AndroidViewModel(app) {
    private val app = getApplication<MyApplication>()

    val currentUser = auth.currentUserProfile

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun initGooglePlaces() {
        // Initialize Places.
        val apiKey = app.getString(R.string.google_maps_key)
        Places.initialize(app, apiKey)
        Places.createClient(app)
    }
}