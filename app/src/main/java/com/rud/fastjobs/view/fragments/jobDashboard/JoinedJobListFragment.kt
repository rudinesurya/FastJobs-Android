package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import com.rud.fastjobs.view.activities.JobDetailActivity

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

    override fun onItemClick(id: String) {
        val intent = Intent(this@JoinedJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
