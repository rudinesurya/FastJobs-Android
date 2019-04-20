package com.rud.fastjobs.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rud.fastjobs.data.network.response.NearbyPlacesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class NearbyPlacesDataSource(
    private val nearbyPlacesApiService: NearbyPlacesApiService
) {
    private val _downloadedNearbyPlaces = MutableLiveData<NearbyPlacesResponse>()
    val downloadedNearbyPlaces: LiveData<NearbyPlacesResponse>
        get() = _downloadedNearbyPlaces

    fun fetchNearbyPlaces(location: String, radius: String, type: String) {
        nearbyPlacesApiService.getNearbyPlaces(location = location, radius = radius, type = type).enqueue(object :
            Callback<NearbyPlacesResponse> {
            override fun onResponse(call: Call<NearbyPlacesResponse>, response: Response<NearbyPlacesResponse>) {
                _downloadedNearbyPlaces.postValue(response.body()!!)
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }
}