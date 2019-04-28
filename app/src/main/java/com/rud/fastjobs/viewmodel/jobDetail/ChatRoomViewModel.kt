package com.rud.fastjobs.viewmodel.jobDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.Store

class ChatRoomViewModel(
    private val myRepository: MyRepository,
    store: Store,
    private val auth: Auth,
    app: Application
) : AndroidViewModel(app) {
    val currentUser = auth.currentUserProfile
    lateinit var jobId: String

    fun getAllCommentsLiveData(onComplete: (LiveData<FirestoreResource<List<Comment>>>) -> Unit = {}) {
        myRepository.getAllCommentsLiveData(jobId, onComplete)
    }

    fun postComment(
        jobId: String,
        text: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        myRepository.postComment(currentUser.value!!, jobId, text, onSuccess, onFailure)
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