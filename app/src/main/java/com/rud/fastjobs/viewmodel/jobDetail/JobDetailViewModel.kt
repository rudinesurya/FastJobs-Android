package com.rud.fastjobs.viewmodel.jobDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.Store

class JobDetailViewModel(
    private val myRepository: MyRepository,
    store: Store,
    private val auth: Auth,
    app: Application
) : AndroidViewModel(app) {
    val currentUser = auth.currentUserProfile
    lateinit var currentJob: Job

    fun getJobById(id: String, onSuccess: (Job?) -> Unit = {}) {
        myRepository.getJobById(id, onSuccess = {
            currentJob = it!!
            onSuccess(it)
        })
    }

    fun joinJob(jobId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        myRepository.joinJob(currentUser.value!!, jobId, onSuccess, onFailure)
    }

    fun leaveJob(
        jobId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.leaveJob(currentUser.value!!.id!!, jobId, onSuccess, onFailure)
    }

    fun addFav(
        jobId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.addFav(currentUser.value!!.id!!, jobId, onSuccess, onFailure)
    }

    fun deleteFav(
        jobId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.deleteFav(currentUser.value!!.id!!, jobId, onSuccess, onFailure)
    }
}