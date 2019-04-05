package com.rud.fastjobs.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rud.fastjobs.R
import timber.log.Timber


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    val VENUE_DETAIL = "VENUE_DETAIL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val venueName = intent.extras.getString("VENUE_NAME")
        val venueLat = intent.extras.getDouble("VENUE_LAT")
        val venueLng = intent.extras.getDouble("VENUE_LNG")

        Timber.d(venueName + ", " + venueLat + ", " + venueLng)

        // Add a marker in Sydney and move the camera
        val latLng = LatLng(venueLat, venueLng)
        mMap.addMarker(MarkerOptions().position(latLng).title("Here"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))

    }
}
