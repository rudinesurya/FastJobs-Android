package com.rud.fastjobs.viewmodel

import androidx.lifecycle.ViewModel
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository


class JobDetailViewModel(private val myRepository: MyRepository) : ViewModel() {
    lateinit var currentJob: Job

    fun getJobById(id: String, onSuccess: (Job?) -> Unit) {
        myRepository.getJobById(id, onSuccess = {
            onSuccess(it)
        }, onFailure = {})
    }
}