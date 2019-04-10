package com.rud.fastjobs.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ptrbrynt.firestorelivedata.ResourceObserver
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.recyclerView.RecommendationCarouselController
import com.rud.fastjobs.viewmodel.JobDetailViewModel
import kotlinx.android.synthetic.main.fragment_job_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class JobDetailFragment : Fragment(), KodeinAware, OnMapReadyCallback {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobDetailViewModel
    private val controller = RecommendationCarouselController()

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
                updateUI()
            }
        }
    }

    fun updateUI() {
        val job = viewModel.currentJob

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        collapsing_toolbar.title = job.title

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val ldt = job.date?.toLocalDateTime()!!
        val dateString = ldt.format(formatter)
        date.text = dateString!!

        date.setOnClickListener {
            val startMillis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val intent = Intent(Intent.ACTION_EDIT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.Events.TITLE, job.title)
                .putExtra(CalendarContract.Events.DESCRIPTION, job.description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, job.venue?.name)

            startActivity(intent)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fab.setOnClickListener {
            val action = JobDetailFragmentDirections.actionEdit(viewModel.currentJob.id)
            findNavController().navigate(action)
        }

        carousel.setController(controller)

        viewModel.getAllJobsLiveData { jobs ->
            jobs.observe(this, object : ResourceObserver<List<Job>> {
                override fun onSuccess(jobs: List<Job>?) {
                    Timber.d("jobs changes observed")
                    Timber.d(jobs?.toString())

                    controller.setData(jobs)
                }

                override fun onLoading() {
                    Timber.d("loading jobs observer")
                }

                override fun onError(throwable: Throwable?, errorMessage: String?) {
                    Timber.e(errorMessage)
                }
            })
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