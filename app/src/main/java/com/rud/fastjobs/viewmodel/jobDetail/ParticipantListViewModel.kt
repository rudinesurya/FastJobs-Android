package com.rud.fastjobs.viewmodel.jobDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.Participant
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository

class ParticipantListViewModel(private val myRepository: MyRepository, app: Application) : AndroidViewModel(app) {
    lateinit var jobId: String

    fun getUserById(id: String, onSuccess: (User?) -> Unit = {}) {
        myRepository.getUserById(id, onSuccess)
    }

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun getAllParticipantsLiveData(onComplete: (LiveData<FirestoreResource<List<Participant>>>) -> Unit = {}) {
        myRepository.getAllParticipantsLiveData(jobId, onComplete)
    }
}