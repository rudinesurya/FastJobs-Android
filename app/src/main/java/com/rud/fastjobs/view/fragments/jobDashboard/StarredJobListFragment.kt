package com.rud.fastjobs.view.fragments.jobDashboard

import com.rud.fastjobs.data.model.Job

class StarredJobListFragment : JobListFragment() {
    override fun setData(jobs: List<Job>) {
        val list = viewModel.currentUser.value?.favList
        controller.setData(
            jobs.filter { list?.contains(it.id) ?: true }.sortedBy { it.date },
            viewModel.currentUser.value
        )
    }
}
