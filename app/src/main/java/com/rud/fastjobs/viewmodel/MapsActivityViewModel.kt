package com.rud.fastjobs.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rud.fastjobs.data.network.response.NearbyPlacesResponse
import com.rud.fastjobs.data.repository.MyRepository

class MapsActivityViewModel(
    private val myRepository: MyRepository,
    app: Application
) : AndroidViewModel(app) {
    lateinit var myLastLocation: Location

    private val _downloadedNearbyPlaces = MutableLiveData<NearbyPlacesResponse>()
    val downloadedNearbyPlaces: LiveData<NearbyPlacesResponse>
        get() = _downloadedNearbyPlaces

    fun fetchNearbyMarket() {
        val lat = myLastLocation.latitude
        val lng = myLastLocation.longitude
        val location = "$lat,$lng"
        val radius = "1500"
        val type = "restaurant"

        // fetch the data. when it is ready, it will be populated to the livedata
        myRepository.fetchNearbyPlaces(location = location, radius = radius, type = type, onSuccess = {
            _downloadedNearbyPlaces.postValue(it)
        })
    }
}