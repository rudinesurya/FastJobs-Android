package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ptrbrynt.firestorelivedata.ResourceObserver
import com.rud.coffeemate.ui.fragments.ScopedFragment
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.activities.JobDetailActivity
import com.rud.fastjobs.view.recyclerViewController.PastJobListEpoxyController
import com.rud.fastjobs.viewmodel.JobListViewModel
import kotlinx.android.synthetic.main.fragment_job_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class PastJobListFragment : ScopedFragment(), KodeinAware, PastJobListEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobListViewModel
    private val controller = PastJobListEpoxyController(this)

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

    private fun initRecyclerView(view: View) {
        allJobs_recyclerView.setController(controller)
    }

    override fun onItemClick(id: String) {
        Timber.d("job [%s] clicked!", id)
        val intent = Intent(this@PastJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
