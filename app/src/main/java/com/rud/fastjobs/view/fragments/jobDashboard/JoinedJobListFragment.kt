package com.rud.fastjobs.view.fragments.jobDashboard

import com.rud.fastjobs.data.model.Job

class JoinedJobListFragment : JobListFragment() {
    override fun setData(jobs: List<Job>) {
        val list = viewModel.currentUser.value?.joinedList
        controller.setData(
            jobs.filter {
                list?.contains(it.id) ?: false
            }.sortedBy { it.date },
            viewModel.currentUser.value
        )
    }
}