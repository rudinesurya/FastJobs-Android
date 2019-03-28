package com.rud.fastjobs.viewmodel

import androidx.lifecycle.ViewModel
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository
import timber.log.Timber


class JobRegistrationViewModel(private val myRepository: MyRepository) : ViewModel() {
    var currentJob: Job? = null

    fun getJobById(id: String, onSuccess: (job: Job?) -> Unit) {
        myRepository.getJobById(id, onSuccess)
    }

    fun addJob(job: Job) {
        myRepository.addJob(job) {
            if (it.isSuccessful) {
                Timber.d("Job added")
            }
        }
    }

    fun handleSave(title: String, payout: Double, description: String, urgency: Boolean) {
        val jobFieldMap = mutableMapOf<String, Any>()

        if (title.isNotBlank() && title != currentJob!!.title)
            jobFieldMap["title"] = title
        if (description.isNotBlank() && description != currentJob!!.description)
            jobFieldMap["description"] = description
        if (payout != currentJob!!.payout)
            jobFieldMap["payout"] = payout
        if (urgency != currentJob!!.urgency)
            jobFieldMap["urgency"] = urgency

        myRepository.updateJob(currentJob!!.id!!, jobFieldMap)
    }

    fun updateJob(id: String, jobFieldMap: Map<String, Any>) {
        myRepository.updateJob(id, jobFieldMap)
    }
}