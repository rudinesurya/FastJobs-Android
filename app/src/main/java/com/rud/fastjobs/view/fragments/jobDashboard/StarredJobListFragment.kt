package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.activities.JobDetailActivity

class StarredJobListFragment : JobListFragment() {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        updateUI()
    }

    override fun applyTransformation(jobs: List<Job>): List<Job> {
        val list = viewModel.currentUser.value?.favList
        return jobs.filter { list?.contains(it.id) ?: true }.sortedBy { it.date }
    }

    override fun onItemClick(id: String) {
        val intent = Intent(this@StarredJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
