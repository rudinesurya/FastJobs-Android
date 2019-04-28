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
import com.rud.fastjobs.utils.FragmentLifecycle
import com.rud.fastjobs.view.activities.JobDetailActivity
import com.rud.fastjobs.view.recyclerViewController.JobListEpoxyController
import com.rud.fastjobs.viewmodel.jobDashboard.JobListViewModel
import kotlinx.android.synthetic.main.fragment_job_list.*
import kotlinx.android.synthetic.main.job_item_card.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

abstract class JobListFragment : ScopedFragment(), KodeinAware, FragmentLifecycle,
    JobListEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    lateinit var viewModel: JobListViewModel
    val controller = JobListEpoxyController(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobListViewModel::class.java)

        initRecyclerView(view)

        viewModel.jobs.observe(this, Observer { jobs ->
            jobs?.let { jobs ->
                controller.setData(jobs, viewModel.currentUser.value)
            }
        })
    }

    private fun initRecyclerView(view: View) {
        allJobs_recyclerView.setController(controller)
    }

    override fun onItemClick(id: String) {
        val intent = Intent(this@JobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun onFavChecked(id: String) {
        if (checkbox_fav.isChecked) viewModel.addFav(id)
        else viewModel.deleteFav(id)
    }
}
