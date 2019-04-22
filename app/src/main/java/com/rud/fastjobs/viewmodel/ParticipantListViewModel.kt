package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.Participant
import com.rud.fastjobs.data.repository.MyRepository

class ParticipantListViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    lateinit var jobId: String

    fun getAllParticipantsLiveData(onComplete: (LiveData<FirestoreResource<List<Participant>>>) -> Unit = {}) {
        myRepository.getAllParticipantsLiveData(jobId, onComplete)
    }
}