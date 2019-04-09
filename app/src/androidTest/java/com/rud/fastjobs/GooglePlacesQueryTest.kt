package com.rud.fastjobs

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rud.fastjobs.data.network.ConnectivityInterceptor
import com.rud.fastjobs.data.network.NearbyPlacesApiService
import com.rud.fastjobs.data.network.response.NearbyPlacesResponse
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


@RunWith(AndroidJUnit4::class)
class GooglePlacesQueryTest {
    lateinit var appContext: Context
    lateinit var apiKey: String

    @BeforeClass
    fun init() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testGoogleApiKeyIsWorking() {
        val connectivityInterceptor = ConnectivityInterceptor(appContext)
        val nearbyPlacesApiService = NearbyPlacesApiService(connectivityInterceptor)

        val location = "-33.8670522,151.1957362"
        val radius = "1500"
        val type = "restaurant"

        nearbyPlacesApiService.getNearbyPlaces(location = location, radius = radius, type = type).enqueue(object :
            Callback<NearbyPlacesResponse> {
            override fun onResponse(call: Call<NearbyPlacesResponse>, response: Response<NearbyPlacesResponse>) {
                Timber.d(response.body()!!.results.first().name)
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }
}