package com.rud.fastjobs.viewmodel.jobDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.Store

class JobDetailViewModel(
    private val myRepository: MyRepository,
    store: Store,
    private val auth: Auth,
    app: Application
) : AndroidViewModel(app) {
    val currentUser = auth.currentUserProfile
    var currentJob: Job? = null

    // fun getJobById(id: String, onSuccess: (Job?) -> Unit) {
    //     myRepository.getJobById(id, onSuccess = {
    //         currentJob = it!!
    //         onSuccess(it)
    //     })
    // }

    fun getUserById(id: String, onSuccess: (User?) -> Unit = {}) {
        myRepository.getUserById(id, onSuccess)
    }

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun getJobByIdLiveData(id: String, onComplete: (LiveData<FirestoreResource<Job>>) -> Unit = {}) {
        myRepository.getJobByIdLiveData(id, onComplete)
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

    fun cancelJob(
        jobId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.cancelJob(jobId, onSuccess, onFailure)
    }

    fun resumeJob(
        jobId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.resumeJob(jobId, onSuccess, onFailure)
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