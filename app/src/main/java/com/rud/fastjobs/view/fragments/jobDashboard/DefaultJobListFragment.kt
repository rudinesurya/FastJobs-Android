package com.rud.fastjobs.view.fragments.jobDashboard

class DefaultJobListFragment : JobListFragment() {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        controller.setData(viewModel.jobs.value?.sortedBy { it.date }, viewModel.currentUser.value)
    }
}
