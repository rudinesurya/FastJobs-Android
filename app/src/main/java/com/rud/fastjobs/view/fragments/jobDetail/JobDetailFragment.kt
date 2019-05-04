package com.rud.fastjobs.view.fragments.jobDetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.utils.FragmentLifecycle
import com.rud.fastjobs.utils.ScopedFragment
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.activities.JobRegistrationActivity
import com.rud.fastjobs.view.activities.PhotoActivity
import com.rud.fastjobs.view.glide.GlideApp
import com.rud.fastjobs.view.recyclerViewController.JobDetailEpoxyController
import com.rud.fastjobs.viewmodel.jobDetail.JobDetailViewModel
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.fragment_job_detail.*
import kotlinx.android.synthetic.main.job_header_info.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class JobDetailFragment : ScopedFragment(), KodeinAware, FragmentLifecycle,
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
            viewModel.getJobByIdLiveData(it) {
                it.observe(this, Observer {
                    it.data?.let {
                        viewModel.currentJob = it
                        updateUI()
                    }
                })
            }
        }

        activity?.fab?.setOnClickListener {
            if (viewModel.currentUser.value?.id != viewModel.currentJob?.hostUid) {
                Toast.makeText(this.context, "Unauthorized", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this.context, JobRegistrationActivity::class.java)
                intent.putExtra("id", viewModel.currentJob?.id)
                startActivity(intent)
            }
        }
    }

    fun updateUI() {
        val job = viewModel.currentJob
        // Timber.d(job.toString())

        controller.setData(job, viewModel.currentUser.value!!, null)
        jobDetail_recyclerView.setController(controller)


        job?.photoUrls?.let {
            if (it.count() > 0) {
                // when there is attached images
                val photoUrl = it[0]
                activity?.image_backdrop?.let {
                    GlideApp.with(this).load(viewModel.pathToReference(photoUrl))
                        .into(it)
                }
            }
        }

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val ldt = job?.date?.toLocalDateTime()!!
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

    override fun onCarouselItemClick(photoUrl: String) {
        // Timber.d("job [%s] clicked!", id)
        val intent = Intent(this.context, PhotoActivity::class.java)
        intent.putExtra("url", photoUrl)
        startActivity(intent)
    }

    override fun onFavChecked() {
        if (checkbox_fav.isChecked) viewModel.addFav(viewModel.currentJob?.id!!)
        else viewModel.deleteFav(viewModel.currentJob?.id!!)
    }

    override fun onJoinBtnClick() {
        viewModel.joinJob(viewModel.currentJob?.id!!, onSuccess = {
            updateUI()
            // Timber.d("join success")
        })
    }

    override fun onLeaveBtnClick() {
        viewModel.leaveJob(viewModel.currentJob?.id!!, onSuccess = {
            updateUI()
            // Timber.d("leave success")
        })
    }

    override fun onCancelBtnClick() {
        viewModel.cancelJob(viewModel.currentJob?.id!!, onSuccess = {
            // updateUI()
            Timber.d("cancel success")
        })
    }

    override fun onResumeBtnClick() {
        viewModel.resumeJob(viewModel.currentJob?.id!!, onSuccess = {
            // updateUI()
            Timber.d("resume success")
        })
    }
}
