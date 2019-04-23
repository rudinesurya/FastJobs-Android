package com.rud.fastjobs.view.fragments.jobDashboard

import android.content.Intent
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.utils.toTimestamp
import com.rud.fastjobs.view.activities.JobDetailActivity
import timber.log.Timber
import java.time.LocalDateTime

class PastJobListFragment : JobListFragment() {
    override fun applyTransformation(jobs: List<Job>): List<Job> {
        return jobs.filter { it.date!! <= LocalDateTime.now().toTimestamp() }.sortedBy { it.date }
    }

    override fun onItemClick(id: String) {
        Timber.d("job [%s] clicked!", id)
        val intent = Intent(this@PastJobListFragment.context, JobDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
