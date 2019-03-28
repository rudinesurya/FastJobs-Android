package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.Job


class JobDao(private val firestoreInstance: FirebaseFirestore) {
    private val jobsRef: CollectionReference = firestoreInstance.collection("jobs")

    fun getAllJobsLiveData(onComplete: (jobs: LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        val result = jobsRef.asLiveData<Job>()
        onComplete(result)
    }

    fun addJob(job: Job, onComplete: (Task<DocumentReference>) -> Unit) {
        jobsRef.add(job).addOnCompleteListener {
            onComplete(it)
        }
    }

    fun updateJob(id: String, jobFieldMap: Map<String, Any>) {
        jobsRef.document(id).update(jobFieldMap)
    }

    fun deleteJob(id: String) {
        jobsRef.document(id).delete()
    }

    fun getJobById(id: String, onSuccess: (job: Job?) -> Unit) {
        jobsRef.document(id).get().addOnSuccessListener {
            var job = it.toObject(Job::class.java)
            job?.id = id
            onSuccess(job)
        }
    }
}