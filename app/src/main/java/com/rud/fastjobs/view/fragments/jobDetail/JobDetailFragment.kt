package com.rud.fastjobs.view.fragments.jobDetail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rud.fastjobs.R
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
import kotlinx.android.synthetic.main.profile_modal.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.time.ZoneId

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
        (activity as AppCompatActivity).supportActionBar?.title = job?.title

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
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setMessage("Cancel this job?")
            setPositiveButton("OK") { _, _ ->
                viewModel.cancelJob(viewModel.currentJob?.id!!, onSuccess = {
                    Timber.d("cancel success")
                })
            }
            setNegativeButton("Cancel", null)
            show()
        }
    }

    override fun onResumeBtnClick() {
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setMessage("Resume this job?")
            setPositiveButton("OK") { _, _ ->
                viewModel.resumeJob(viewModel.currentJob?.id!!, onSuccess = {
                    Timber.d("resume success")
                })
            }
            setNegativeButton("Cancel", null)
            show()
        }
    }

    override fun onDateBtnClick() {
        val builder = AlertDialog.Builder(context)

        with(builder)
        {
            setMessage("Do you want to add this job to Calendar?")
            setPositiveButton("OK") { _, _ ->
                val job = viewModel.currentJob!!
                val ldt = job.date?.toLocalDateTime()
                val startMillis = ldt?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

                val intent = Intent(Intent.ACTION_EDIT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                    .putExtra(CalendarContract.Events.TITLE, job.title)
                    .putExtra(CalendarContract.Events.DESCRIPTION, job.description)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, job.venue?.name)

                startActivity(intent)
            }
            setNegativeButton("Cancel", null)
            show()
        }
    }

    override fun onLocationBtnClick() {
        activity?.app_bar_layout?.setExpanded(false)
        jobDetail_recyclerView.scrollToPosition(3)
    }

    override fun onHostBtnClick() {
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.profile_modal, null)
        val job = viewModel.currentJob!!

        viewModel.getUserById(job.hostUid) { user ->
            user?.let {
                // set the image avatar
                if (user.avatarUrl.isNotBlank()) {
                    GlideApp.with(this).load(viewModel.pathToReference(user.avatarUrl))
                        .transforms(CenterCrop(), RoundedCorners(1000))
                        .into(view.image_avatar)
                }
                //set the bio
                view.text_name.text = user.name
                view.text_bio.text = user.bio
                view.text_location.text = user.location?.address
            }
        }

        with(builder)
        {
            setView(view)
            setMessage("")
            setPositiveButton("Ok", null)
            show()
        }
    }
}
