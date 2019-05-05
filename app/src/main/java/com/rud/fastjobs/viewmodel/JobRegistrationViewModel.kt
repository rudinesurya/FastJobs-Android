package com.rud.fastjobs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.Venue
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.utils.toTimestamp
import timber.log.Timber
import java.time.LocalDateTime

class JobRegistrationViewModel(private val myRepository: MyRepository, private val auth: Auth, app: Application) :
    AndroidViewModel(app) {
    var currentJob: Job? = null
    var currentSelectedVenue: Venue? = null
    var currentSelectedDate: LocalDateTime? = null
    val selectedImageBytesArray = mutableListOf<ByteArray>()

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
        val urls = mutableListOf<String>()
        if (selectedImageBytesArray.count() == 0) {
            finaliseSave(title, payout, description, urgency, urls)
            return
        }

        var imagesUploaded = 0
        selectedImageBytesArray.forEach { ba ->
            myRepository.uploadPhoto(auth.currentUserProfile.value?.id!!, ba, onSuccess = { imagePath ->
                ++imagesUploaded
                urls.add(imagePath)

                if (imagesUploaded == selectedImageBytesArray.count()) {
                    Timber.d("All photos have been uploaded")
                    finaliseSave(title, payout, description, urgency, urls)
                }
            }, onFailure = {
                Timber.e(it)
            })
        }
    }

    fun finaliseSave(title: String, payout: Double, description: String, urgency: Boolean, urls: MutableList<String>) {
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
            if (urls.count() > 0)
                jobFieldMap["photoUrls"] = urls

            if (jobFieldMap.count() > 0)
                updateJob(currentJob!!.id!!, jobFieldMap)
            else
                Toast.makeText(getApplication(), "Nothing changes", Toast.LENGTH_SHORT).show()
        } else {
            val newJob = Job(
                title = title,
                hostName = auth.currentUserProfile.value?.name ?: "",
                hostUid = auth.currentUserProfile.value?.id ?: "",
                hostAvatarUrl = auth.currentUserProfile.value?.avatarUrl ?: "",
                description = description,
                payout = payout,
                urgency = urgency,
                venue = currentSelectedVenue,
                date = currentSelectedDate!!.toTimestamp(),
                photoUrls = urls
            )
            addJob(newJob)
        }
    }
}