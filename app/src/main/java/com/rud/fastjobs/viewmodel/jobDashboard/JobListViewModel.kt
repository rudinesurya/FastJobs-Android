package com.rud.fastjobs.viewmodel.jobDashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository

class JobListViewModel(
    private val myRepository: MyRepository,
    private val auth: Auth,
    app: Application
) : AndroidViewModel(app) {
    lateinit var currentUser: User

    fun getCurrentUserLiveData(onComplete: (LiveData<FirestoreResource<User>>) -> Unit = {}) {
        myRepository.getUserByIdLiveData(auth.currentUser?.uid!!, onComplete)
    }

    fun getAllJobsLiveData(onComplete: (LiveData<FirestoreResource<List<Job>>>) -> Unit = {}) {
        myRepository.getAllJobsLiveData(onComplete)
    }
}