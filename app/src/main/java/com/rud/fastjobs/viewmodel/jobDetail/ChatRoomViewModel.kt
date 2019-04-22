package com.rud.fastjobs.viewmodel.jobDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository

class ChatRoomViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    lateinit var currentUser: User
    lateinit var jobId: String

    fun getUserById(id: String, onSuccess: (User?) -> Unit = {}) {
        myRepository.getUserById(id, onSuccess = {
            currentUser = it!!
            onSuccess(it)
        })
    }

    fun getAllCommentsLiveData(onComplete: (LiveData<FirestoreResource<List<Comment>>>) -> Unit = {}) {
        myRepository.getAllCommentsLiveData(jobId, onComplete)
    }

    fun postComment(
        jobId: String,
        text: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.postComment(currentUser, jobId, text, onSuccess, onFailure)
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