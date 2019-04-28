package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import com.rud.fastjobs.utils.toTimestamp
import com.rud.fastjobs.view.activities.JobDetailActivity
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

    override fun onItemClick(id: String) {
        val intent = Intent(this@PastJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
