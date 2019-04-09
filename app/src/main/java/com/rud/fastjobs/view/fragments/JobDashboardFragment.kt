package com.rud.fastjobs.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ptrbrynt.firestorelivedata.ResourceObserver
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.network.NearbyPlacesDataSource
import com.rud.fastjobs.view.recyclerView.JobListController
import com.rud.fastjobs.viewmodel.JobListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_job_dashboard.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber


class JobDashboardFragment : Fragment(), KodeinAware, JobListController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private val nearbyPlacesDataSource: NearbyPlacesDataSource by instance()
    private lateinit var viewModel: JobListViewModel
    private val controller: JobListController = JobListController(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobListViewModel::class.java)
        Timber.d("onViewCreated")

        //TEST
        nearbyPlacesDataSource.fetchNearbyPlaces("-33.8670522,151.1957362", "1500", "restaurant")
        nearbyPlacesDataSource.downloadedNearbyPlaces.observe(this@JobDashboardFragment, Observer {
            Timber.d(it.results.first().toString())
        })

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
            toolbar.title = "Fast Jobs"
        }

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
        recycler_view.setController(controller)
    }

    override fun onItemClick(id: String) {
        Timber.d("job [%s] clicked!", id)
        val action = JobDashboardFragmentDirections.actionOnItemClick(id)
        findNavController().navigate(action)
    }
}
