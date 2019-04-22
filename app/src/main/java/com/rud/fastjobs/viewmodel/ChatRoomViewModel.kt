package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.data.repository.MyRepository

class ChatRoomViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    lateinit var jobId: String

    fun getAllCommentsLiveData(onComplete: (LiveData<FirestoreResource<List<Comment>>>) -> Unit = {}) {
        myRepository.getAllCommentsLiveData(jobId, onComplete)
    }
}