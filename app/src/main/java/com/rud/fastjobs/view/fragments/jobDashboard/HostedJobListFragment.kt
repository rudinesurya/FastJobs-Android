package com.rud.fastjobs.view.fragments.jobDashboard

import com.rud.fastjobs.data.model.Job

class HostedJobListFragment : JobListFragment() {
    override fun setData(jobs: List<Job>) {
        controller.setData(
            jobs.filter {
                it.hostUid == viewModel.currentUser.value?.id
            }.sortedBy { it.date },
            viewModel.currentUser.value
        )
    }
}