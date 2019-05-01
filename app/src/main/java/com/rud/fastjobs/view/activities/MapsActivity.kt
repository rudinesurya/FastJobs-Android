package com.rud.fastjobs.view.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.InfoWindowData
import com.rud.fastjobs.data.model.Placemark
import com.rud.fastjobs.view.miscs.MyInfoWindowAdapter
import com.rud.fastjobs.viewmodel.MapsActivityViewModel
import kotlinx.android.synthetic.main.activity_maps.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class MapsActivity : AppCompatActivity(), KodeinAware, OnMapReadyCallback {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: MapsActivityViewModel
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null

    // Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private val nearbyPlacesMarkers = mutableListOf<Marker>()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MapsActivityViewModel::class.java)

        viewModel.nearbyPlaces.observe(this, Observer {
            placeMarkers(it)
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                viewModel.myLastLocation = p0.lastLocation
                // Timber.d("current: ${viewModel.myLastLocation}")
                // setCurrentPositionOnMap(LatLng(viewModel.myLastLocation.latitude, viewModel.myLastLocation.longitude))
            }
        }

        createLocationRequest()

        botNavView.menu.getItem(0).isCheckable = false
        botNavView.setOnNavigationItemSelectedListener { item ->
            item.isCheckable = true
            when (item.itemId) {
                R.id.action_nearbyJobs -> selectNearByPlaces("job")
                R.id.action_nearbyRestaurant -> selectNearByPlaces("restaurant")
                R.id.action_nearbyBar -> selectNearByPlaces("bar")
                else -> selectNearByPlaces("market")
            }
        }
    }

    private fun getVisibleRegion(): Pair<LatLng, Double> {
        val visibleRegion = mMap.projection.visibleRegion
        val distanceWidth = FloatArray(1)
        val farLeft: LatLng = visibleRegion.farLeft
        val farRight: LatLng = visibleRegion.farRight

        Location.distanceBetween(
            farLeft.latitude,
            farLeft.longitude,
            farRight.latitude,
            farRight.longitude,
            distanceWidth
        )

        val latLng = visibleRegion.latLngBounds.center
        val radius = distanceWidth[0] / 2.0

        return Pair(latLng, radius)
    }

    private fun selectNearByPlaces(keyword: String): Boolean {
        val result = getVisibleRegion()
        val latLng = result.first
        val radius = result.second

        when (keyword) {
            "job" -> viewModel.fetchNearbyJobs()
            "restaurant" -> viewModel.fetchNearbyPlacesFromGoogle(latLng, radius, "restaurant")
            "bar" -> viewModel.fetchNearbyPlacesFromGoogle(latLng, radius, "bar")
        }

        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        val customInfoWindow = MyInfoWindowAdapter(this)
        mMap.setInfoWindowAdapter(customInfoWindow)
        mMap.setOnInfoWindowClickListener {
            val infoWindowData: InfoWindowData? = it?.tag as InfoWindowData?
            Timber.d(infoWindowData.toString())
            if (infoWindowData?.job != null) {
                Timber.d("launching job detail activity")
                val intent = Intent(this, JobDetailActivity::class.java)
                intent.putExtra("id", infoWindowData.job.id)
                startActivity(intent)
            }
        }

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true

        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                viewModel.myLastLocation = location

                val latLng = LatLng(location.latitude, location.longitude)
                setCurrentPositionOnMap(latLng)
            }
        }
    }

    private fun placeMarkers(places: List<Placemark>) {
        nearbyPlacesMarkers.forEach {
            it.remove()
        }
        nearbyPlacesMarkers.clear()

        places.forEach {
            val latLng = LatLng(it.lat, it.lng)
            val markerOptions = MarkerOptions().position(latLng)
            markerOptions.apply {

            }
            val m = mMap.addMarker(markerOptions)
            m.tag = InfoWindowData(locationName = it.name, job = it.job)
            nearbyPlacesMarkers.add(m)
        }
    }

    private fun setCurrentPositionOnMap(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng)
        markerOptions.apply {
            title("You")
        }

        if (marker != null)
            marker!!.remove()

        marker = mMap.addMarker(markerOptions)
        marker?.tag = InfoWindowData(locationName = "You")
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        this@MapsActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }
}
