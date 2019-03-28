package com.rud.fastjobs.viewmodel

import androidx.lifecycle.ViewModel
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository
import timber.log.Timber


class JobRegistrationViewModel(private val myRepository: MyRepository) : ViewModel() {
    fun addJob(job: Job) {
        myRepository.addJob(job) {
            if (it.isSuccessful) {
                Timber.d("Job added")
            }
        }
    }
}