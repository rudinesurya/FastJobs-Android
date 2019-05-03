package com.rud.fastjobs.view.fragments.jobDashboard

import com.rud.fastjobs.data.model.Job

class DefaultJobListFragment : JobListFragment() {
    override fun setData(jobs: List<Job>) {
        controller.setData(jobs.sortedBy { it.date }, viewModel.currentUser.value)
    }
}
