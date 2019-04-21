package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.repository.MyRepository

class JobDetailViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    lateinit var currentJob: Job

    fun getJobById(id: String, onSuccess: (Job?) -> Unit = {}) {
        myRepository.getJobById(id, onSuccess)
    }

    fun joinJob(userId: String, jobId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        myRepository.joinJob(userId, jobId, onSuccess, onFailure)
    }

    fun leaveJob(
        userId: String,
        jobId: String,
        participantId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.leaveJob(userId, jobId, participantId, onSuccess, onFailure)
    }

    fun postComment(
        userId: String,
        jobId: String,
        text: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.postComment(userId, jobId, text, onSuccess, onFailure)
    }

    fun deleteComment(
        jobId: String,
        commentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        myRepository.deleteComment(jobId, commentId, onSuccess, onFailure)
    }
}