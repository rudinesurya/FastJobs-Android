package com.rud.fastjobs.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.MyApplication
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.Placemark
import com.rud.fastjobs.data.repository.MyRepository

class MapsActivityViewModel(
    private val myRepository: MyRepository,
    app: Application
) : AndroidViewModel(app) {
    private val app = getApplication<MyApplication>()
    lateinit var myLastLocation: Location

    private val _nearbyPlaces = MutableLiveData<List<Placemark>>()
    val nearbyPlaces: LiveData<List<Placemark>>
        get() = _nearbyPlaces

    lateinit var jobs: List<Job>

    fun getAllJobsLiveData(onComplete: (LiveData<FirestoreResource<List<Job>>>) -> Unit = {}) {
        myRepository.getAllJobsLiveData(onComplete)
    }

    fun fetchNearbyJobs() {
        val nearbyPlacemarks =
            jobs.map { Placemark(it.title, it.venue?.geoPoint?.latitude!!, it.venue.geoPoint.longitude, it) }
        _nearbyPlaces.postValue(nearbyPlacemarks)
    }

    fun fetchNearbyPlacesFromGoogle(type: String) {
        val lat = myLastLocation.latitude
        val lng = myLastLocation.longitude
        val location = "$lat,$lng"
        val radius = "2500"

        // fetch the data. when it is ready, it will be populated to the livedata
        myRepository.fetchNearbyPlaces(location = location, radius = radius, type = type, onSuccess = {
            val nearbyPlacemarks = it.results.map {
                val location = it.geometry.location
                Placemark(it.name, location.lat, location.lng)
            }
            _nearbyPlaces.postValue(nearbyPlacemarks)
        })
    }
}