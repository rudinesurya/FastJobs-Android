package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.activities.JobDetailActivity

class DefaultJobListFragment : JobListFragment() {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        updateUI()
    }

    override fun applyTransformation(jobs: List<Job>): List<Job> {
        return jobs.sortedBy { it.date }
    }

    override fun onItemClick(id: String) {
        val intent = Intent(this@DefaultJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
