package com.rud.fastjobs.view.fragments.jobDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rud.coffeemate.ui.fragments.ScopedFragment
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.utils.FragmentLifecycle
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.recyclerViewController.JobDetailEpoxyController
import com.rud.fastjobs.viewmodel.jobDetail.JobDetailViewModel
import kotlinx.android.synthetic.main.fragment_job_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class JobDetailFragment : ScopedFragment(), KodeinAware, FragmentLifecycle, OnMapReadyCallback,
    JobDetailEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobDetailViewModel
    private val controller = JobDetailEpoxyController(this)

    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.rud.fastjobs.R.layout.fragment_job_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobDetailViewModel::class.java)

        activity?.intent?.getStringExtra("id")?.let {
            viewModel.getJobById(it) {
                updateUI()
            }
        }
    }

    fun updateUI() {
        val job = viewModel.currentJob
        Timber.d(job.toString())

        controller.setData(job, viewModel.currentUser.value!!)
        jobDetail_recyclerView.setController(controller)

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val ldt = job.date?.toLocalDateTime()!!
        val dateString = ldt.format(formatter)

//
//        date.text = dateString!!
//
//        date.setOnClickListener {
//            val startMillis = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//
//            val intent = Intent(Intent.ACTION_EDIT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
//                .putExtra(CalendarContract.Events.TITLE, job.title)
//                .putExtra(CalendarContract.Events.DESCRIPTION, job.description)
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, job.venue?.name)
//
//            startActivity(intent)
//        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
//        mapFragment.getMapAsync(this)
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

    override fun onCarouselItemClick(id: String) {
        // Timber.d("job [%s] clicked!", id)
    }

    override fun onShareBtnClick() {
    }

    override fun onJoinBtnClick() {
        viewModel.joinJob(viewModel.currentJob.id!!, onSuccess = {
            // Timber.d("join success")
        })
    }

    override fun onLeaveBtnClick() {
        viewModel.leaveJob(viewModel.currentJob.id!!, onSuccess = {
            // Timber.d("leave success")
        })
    }
}