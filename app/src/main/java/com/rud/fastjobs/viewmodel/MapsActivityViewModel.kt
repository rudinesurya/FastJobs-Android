package com.rud.fastjobs.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.rud.fastjobs.MyApplication
import com.rud.fastjobs.data.model.Placemark
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.Store

class MapsActivityViewModel(
    private val myRepository: MyRepository, val store: Store,
    app: Application
) : AndroidViewModel(app) {
    private val app = getApplication<MyApplication>()
    lateinit var myLastLocation: Location

    private val _nearbyPlaces = MutableLiveData<List<Placemark>>()
    val nearbyPlaces: LiveData<List<Placemark>>
        get() = _nearbyPlaces

    val jobs = store.jobs

    fun fetchNearbyJobs() {
        val nearbyPlacemarks =
            jobs.value?.map { Placemark(it.title, it.venue?.geoPoint?.latitude!!, it.venue.geoPoint.longitude, it) }
        _nearbyPlaces.postValue(nearbyPlacemarks)
    }

    fun fetchNearbyPlacesFromGoogle(location: LatLng, radius: Double, type: String) {
        val lat = location.latitude
        val lng = location.longitude
        val locationStr = "$lat,$lng"
        val radiusStr = radius.toString()

        // fetch the data. when it is ready, it will be populated to the livedata
        myRepository.fetchNearbyPlaces(location = locationStr, radius = radiusStr, type = type, onSuccess = {
            val nearbyPlacemarks = it.results.map {
                val location = it.geometry.location
                Placemark(it.name, location.lat, location.lng)
            }
            _nearbyPlaces.postValue(nearbyPlacemarks)
        })
    }
}