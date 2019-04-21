package com.rud.fastjobs.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.rud.fastjobs.data.network.NearbyPlacesDataSource
import com.rud.fastjobs.data.network.response.NearbyPlacesResponse
import com.rud.fastjobs.data.repository.MyRepository

class MapsActivityViewModel(
    private val myRepository: MyRepository,
    private val nearbyPlacesDataSource: NearbyPlacesDataSource,
    app: Application
) : AndroidViewModel(app) {
    lateinit var myLastLocation: Location

    val downloadedNearbyPlaces: LiveData<NearbyPlacesResponse> = nearbyPlacesDataSource.downloadedNearbyPlaces

    fun fetchNearbyMarket() {
        val lat = myLastLocation.latitude
        val lng = myLastLocation.longitude
        val location = "$lat,$lng"
        val radius = "1500"
        val type = "restaurant"

        // fetch the data. when it is ready, it will be populated to the livedata
        nearbyPlacesDataSource.fetchNearbyPlaces(location = location, radius = radius, type = type)
    }
}