package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository

class JobDetailViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    lateinit var currentJob: Job

    fun getJobById(id: String, onSuccess: (Job?) -> Unit = {}) {
        myRepository.getJobById(id, onSuccess = {
            onSuccess(it)
        })
    }
}