package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rud.coffeemate.ui.fragments.ScopedFragment
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.view.activities.JobDetailActivity
import com.rud.fastjobs.view.recyclerViewController.JobListEpoxyController
import com.rud.fastjobs.viewmodel.jobDashboard.JobListViewModel
import kotlinx.android.synthetic.main.fragment_job_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class JobListFragment : ScopedFragment(), KodeinAware, JobListEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobListViewModel
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
        Timber.d("onViewCreated")

        initRecyclerView(view)
        viewModel.getAllJobsLiveData { jobs ->
            jobs.observe(this, Observer {
                it.data?.let { jobs ->
                    Timber.d("jobs changes observed")
                    Timber.d(jobs.toString())

                    controller.setData(jobs.sortedBy { it.date })
                }
            })
        }
    }

    private fun initRecyclerView(view: View) {
        allJobs_recyclerView.setController(controller)
    }

    override fun onItemClick(id: String) {
        Timber.d("job [%s] clicked!", id)
        val intent = Intent(this@JobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
