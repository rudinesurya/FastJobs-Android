package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.android.libraries.places.api.Places
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.MyApplication
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository

class JobDashboardActivityViewModel(
    private val myRepository: MyRepository, app: Application
) : AndroidViewModel(app) {
    private val app = getApplication<MyApplication>()

    fun getUserByIdLiveData(id: String, onComplete: (LiveData<FirestoreResource<User>>) -> Unit = {}) {
        myRepository.getUserByIdLiveData(id, onComplete)
    }

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun initGooglePlaces() {
        // Initialize Places.
        val apiKey = app.getString(R.string.google_maps_key)
        Places.initialize(app, apiKey)
        Places.createClient(app)
    }
}