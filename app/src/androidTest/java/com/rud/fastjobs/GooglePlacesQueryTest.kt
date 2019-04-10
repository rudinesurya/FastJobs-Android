package com.rud.fastjobs

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.rud.fastjobs.data.network.ConnectivityInterceptor
import com.rud.fastjobs.data.network.NearbyPlacesApiService
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test


class GooglePlacesQueryTest {
    companion object {
        lateinit var appContext: Context
        lateinit var nearbyPlacesApiService: NearbyPlacesApiService

        @BeforeClass
        @JvmStatic
        fun setup() {
            appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val apiKey = appContext.getString(R.string.google_maps_key)
            val connectivityInterceptor = ConnectivityInterceptor(appContext)
            nearbyPlacesApiService = NearbyPlacesApiService(apiKey, connectivityInterceptor)
        }
    }

    @Test
    fun testNearbyPlacesApiService() {
        val location = "-33.8670522,151.1957362"
        val radius = "1500"
        val type = "restaurant"

        val response =
            nearbyPlacesApiService.getNearbyPlaces(location = location, radius = radius, type = type).execute()

        val firstEntry = response.body()!!.results.first()

        Assert.assertEquals("bc58a5ca8e9c65e7b673c6cd628d24828a104be6", firstEntry.id)
        Assert.assertEquals("Travelodge Hotel Sydney Wynyard", firstEntry.name)
    }
}