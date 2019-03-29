package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.Job


class JobDao(private val firestoreInstance: FirebaseFirestore) {
    private val jobsRef: CollectionReference = firestoreInstance.collection("jobs")

    fun getAllJobsLiveData(onComplete: (LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        val result = jobsRef.asLiveData<Job>()
        onComplete(result)
    }

    fun addJob(job: Job, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.add(job).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun updateJob(id: String, jobFieldMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.document(id).update(jobFieldMap).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun deleteJob(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.document(id).delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun getJobById(id: String, onSuccess: (Job) -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.document(id).get().addOnSuccessListener {
            val job = it.toObject(Job::class.java)!!
            job.id = id
            onSuccess(job)
        }.addOnFailureListener {
            onFailure(it)
        }
    }
}