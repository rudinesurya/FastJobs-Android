package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.activities.JobDetailActivity
import timber.log.Timber

class JoinedJobListFragment : JobListFragment() {
    override fun applyTransformation(jobs: List<Job>): List<Job> {
        val list = viewModel.currentUser.joinedList
        return jobs.filter { list.contains(it.id) }.sortedBy { it.date }
    }

    override fun onItemClick(id: String) {
        Timber.d("job [%s] clicked!", id)
        val intent = Intent(this@JoinedJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
