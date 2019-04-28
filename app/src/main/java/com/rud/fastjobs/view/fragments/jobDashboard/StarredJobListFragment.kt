package com.rud.fastjobs.view.fragments.jobDashboard

class StarredJobListFragment : JobListFragment() {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        val list = viewModel.currentUser.value?.favList
        controller.setData(
            viewModel.jobs.value?.filter { list?.contains(it.id) ?: true }?.sortedBy { it.date },
            viewModel.currentUser.value
        )
    }
}
