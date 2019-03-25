package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.Job


class JobDao(private val firestoreInstance: FirebaseFirestore) {
    fun getAllJobs(onSuccess: (jobs: LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        val result = firestoreInstance.collection("jobs").asLiveData<Job>()
        onSuccess(result)
    }
}