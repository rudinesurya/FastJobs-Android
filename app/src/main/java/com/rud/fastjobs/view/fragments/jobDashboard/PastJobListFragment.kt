package com.rud.fastjobs.view.fragments.jobDashboard

import com.rud.fastjobs.utils.toTimestamp
import java.time.LocalDateTime

class PastJobListFragment : JobListFragment() {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        controller.setData(
            viewModel.jobs.value?.filter { it.date!! <= LocalDateTime.now().toTimestamp() }?.sortedBy { it.date },
            viewModel.currentUser.value
        )
    }
}
