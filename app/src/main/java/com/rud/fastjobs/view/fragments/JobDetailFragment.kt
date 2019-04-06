package com.rud.fastjobs.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.viewmodel.JobDetailViewModel
import kotlinx.android.synthetic.main.fragment_job_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class JobDetailFragment : Fragment(), KodeinAware, OnMapReadyCallback {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.rud.fastjobs.R.layout.fragment_job_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobDetailViewModel::class.java)

        arguments?.let {
            val safeArgs = JobDetailFragmentArgs.fromBundle(it)
            viewModel.getJobById(safeArgs.jobId) { job ->
                viewModel.currentJob = job!!

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }

        btn_edit.setOnClickListener {
            val action = JobDetailFragmentDirections.actionEdit(viewModel.currentJob.id)
            findNavController().navigate(action)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val lat = viewModel.currentJob.venue?.geoPoint?.latitude
        val lng = viewModel.currentJob.venue?.geoPoint?.longitude

        googleMap.setOnMapClickListener {
            val gmmIntentUri = Uri.parse("geo:$lat,$lng?z=12")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(this@JobDetailFragment.context?.packageManager) != null) {
                startActivity(mapIntent)
            }
        }

        if (lat != null && lng != null) {
            val latLng = LatLng(lat, lng)
            googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
        }
    }
}