package com.rud.fastjobs.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.viewmodel.JobDetailViewModel
import kotlinx.android.synthetic.main.fragment_job_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class JobDetailFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobDetailViewModel::class.java)

        arguments?.let {
            val safeArgs = JobDetailFragmentArgs.fromBundle(it)
            viewModel.getJobById(safeArgs.jobId) { job ->
                if (job != null)
                    viewModel.currentJob = job
            }
        }

        btn_edit.setOnClickListener {
            val action = JobDetailFragmentDirections.actionEdit(viewModel.currentJob.id)
            findNavController().navigate(action)
        }
    }
}