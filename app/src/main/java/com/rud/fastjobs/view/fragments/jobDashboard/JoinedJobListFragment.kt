package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.activities.JobDetailActivity
import timber.log.Timber

class JoinedJobListFragment : JobListFragment() {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        updateUI()
    }

    override fun applyTransformation(jobs: List<Job>): List<Job> {
        Timber.d(viewModel.currentUser.value?.toString())
        val list = viewModel.currentUser.value?.joinedList
        return jobs.filter { list?.contains(it.id) ?: false }.sortedBy { it.date }
    }

    override fun onItemClick(id: String) {
        val intent = Intent(this@JoinedJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
