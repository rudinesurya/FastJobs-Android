package com.rud.fastjobs.view.fragments.jobDashboard

class JoinedJobListFragment : JobListFragment() {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        val list = viewModel.currentUser.value?.joinedList
        controller.setData(
            viewModel.jobs.value?.filter {
                list?.contains(it.id) ?: false
            }?.sortedBy { it.date },
            viewModel.currentUser.value
        )
    }
}
