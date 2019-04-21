package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository

class JobListViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    fun getAllJobsLiveData(onComplete: (LiveData<FirestoreResource<List<Job>>>) -> Unit = {}) {
        myRepository.getAllJobsLiveData(onComplete)
    }
}