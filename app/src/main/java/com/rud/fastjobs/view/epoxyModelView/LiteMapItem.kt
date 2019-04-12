package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rud.fastjobs.R
import kotlinx.android.synthetic.main.lite_google_map.view.*


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LiteMapItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var marker: Marker? = null
    private var myLocation: LatLng? = null
    private val markerOptions = MarkerOptions()
    private var googleMap: GoogleMap? = null

    init {
        inflate(context, R.layout.lite_google_map, this)
        mapView.isClickable = false
        mapView.isLongClickable = false
    }

    fun startMap() {
        mapView.onCreate(null)
        mapView.getMapAsync {
            googleMap = it
            googleMap?.uiSettings?.isMapToolbarEnabled = false
            myLocation?.let {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12.0f))
                markerOptions.position(it)
                marker?.remove()
                marker = googleMap?.addMarker(markerOptions)
            }
        }
    }

    @ModelProp
    fun setLocation(newLocation: LatLng?) =
        newLocation?.let {
            myLocation = it
            startMap()
        }
}