package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.Job


class JobDao(private val firestoreInstance: FirebaseFirestore) {
    fun getAllJobs(onComplete: (jobs: LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        val result = firestoreInstance.collection("jobs").asLiveData<Job>()
        onComplete(result)
    }

    fun addJob(job: Job, onComplete: (Task<DocumentReference>) -> Unit) {
        firestoreInstance.collection("jobs").add(job).addOnCompleteListener {
            onComplete(it)
        }
    }
}