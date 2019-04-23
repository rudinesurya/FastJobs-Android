package com.rud.fastjobs.view.fragments.jobDashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rud.coffeemate.ui.fragments.ScopedFragment
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.recyclerViewController.JobListEpoxyController
import com.rud.fastjobs.viewmodel.jobDashboard.JobListViewModel
import kotlinx.android.synthetic.main.fragment_job_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

abstract class JobListFragment : ScopedFragment(), KodeinAware, JobListEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    lateinit var viewModel: JobListViewModel
    private val controller = JobListEpoxyController(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobListViewModel::class.java)
        // Timber.d("onViewCreated")

        viewModel.getCurrentUserLiveData { user ->
            user.observe(this, Observer {
                it.data?.let { user ->
                    viewModel.currentUser = user
                }
            })
        }

        initRecyclerView(view)
        viewModel.getAllJobsLiveData { jobs ->
            jobs.observe(this, Observer {
                it.data?.let { jobs ->
                    // Timber.d("jobs changes observed")
                    controller.setData(applyTransformation(jobs))
                }
            })
        }
    }

    open fun applyTransformation(jobs: List<Job>): List<Job> {
        return jobs
    }

    private fun initRecyclerView(view: View) {
        allJobs_recyclerView.setController(controller)
    }

    override fun onItemClick(id: String) {
    }
}
