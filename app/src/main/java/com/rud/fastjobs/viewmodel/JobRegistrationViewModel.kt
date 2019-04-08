package com.rud.fastjobs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.Venue
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.utils.toTimestamp
import java.time.LocalDateTime


class JobRegistrationViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    var currentJob: Job? = null
    var currentSelectedVenue: Venue? = null
    var currentSelectedDate: LocalDateTime? = null

    fun getJobById(id: String, onSuccess: (Job?) -> Unit = {}) {
        myRepository.getJobById(id, onSuccess = {
            currentJob = it
            onSuccess(it)
        })
    }

    fun addJob(job: Job) {
        myRepository.addJob(job, onSuccess = {
            Toast.makeText(getApplication(), "Saved!", Toast.LENGTH_SHORT).show()
        })
    }

    fun updateJob(id: String, jobFieldMap: Map<String, Any>) {
        myRepository.updateJob(id, jobFieldMap, onSuccess = {
            Toast.makeText(getApplication(), "Updated!", Toast.LENGTH_SHORT).show()
        })
    }

    fun handleSave(title: String, payout: Double, description: String, urgency: Boolean) {
        if (currentJob != null) {
            val jobFieldMap = mutableMapOf<String, Any>()

            if (title.isNotBlank() && title != currentJob!!.title)
                jobFieldMap["title"] = title
            if (description.isNotBlank() && description != currentJob!!.description)
                jobFieldMap["description"] = description
            if (payout != currentJob!!.payout)
                jobFieldMap["payout"] = payout
            if (urgency != currentJob!!.urgency)
                jobFieldMap["urgency"] = urgency
            if (currentSelectedVenue != null)
                jobFieldMap["venue"] = currentSelectedVenue!!
            if (currentSelectedDate != null)
                jobFieldMap["date"] = currentSelectedDate!!.toTimestamp()

            updateJob(currentJob!!.id!!, jobFieldMap)
        } else {
            val newJob = Job(
                title = title,
                hostName = "host",
                hostUid = "123",
                hostAvatarUrl = "123",
                description = description,
                payout = payout,
                urgency = urgency,
                venue = currentSelectedVenue,
                date = currentSelectedDate!!.toTimestamp()
            )
            addJob(newJob)
        }
    }
}