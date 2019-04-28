package com.rud.fastjobs.viewmodel.jobDashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.Store

class JobListViewModel(
    private val myRepository: MyRepository,
    store: Store,
    private val auth: Auth,
    app: Application
) : AndroidViewModel(app) {
    val currentUser = auth.currentUserProfile
    val jobs = store.jobs

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