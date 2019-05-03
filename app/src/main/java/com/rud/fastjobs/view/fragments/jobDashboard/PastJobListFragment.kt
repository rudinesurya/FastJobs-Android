package com.rud.fastjobs.view.fragments.jobDashboard

import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.utils.toTimestamp
import java.time.LocalDateTime

class PastJobListFragment : JobListFragment() {
    override fun setData(jobs: List<Job>) {
        controller.setData(
            jobs.filter { it.date!! <= LocalDateTime.now().toTimestamp() }.sortedBy { it.date },
            viewModel.currentUser.value
        )
    }
}
