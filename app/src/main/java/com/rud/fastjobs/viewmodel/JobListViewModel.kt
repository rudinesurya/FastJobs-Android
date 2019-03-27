package com.rud.fastjobs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository


class JobListViewModel(private val myRepository: MyRepository) : ViewModel() {
    fun getAllJobs(onSuccess: (jobs: LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        myRepository.getAllJobs(onSuccess)
    }
}