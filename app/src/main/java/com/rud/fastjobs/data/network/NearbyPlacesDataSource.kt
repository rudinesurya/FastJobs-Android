package com.rud.fastjobs.data.network

import com.rud.fastjobs.data.network.response.NearbyPlacesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Data access object for getting google places
 */
class NearbyPlacesDataSource(
    private val nearbyPlacesApiService: NearbyPlacesApiService
) {
    fun fetchNearbyPlaces(
        location: String,
        radius: String,
        type: String,
        onSuccess: (NearbyPlacesResponse) -> Unit = {},
        onFailure: (Throwable) -> Unit = {}
    ) {
        nearbyPlacesApiService.getNearbyPlaces(location = location, radius = radius, type = type).enqueue(object :
            Callback<NearbyPlacesResponse> {
            override fun onResponse(call: Call<NearbyPlacesResponse>, response: Response<NearbyPlacesResponse>) {
                onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}