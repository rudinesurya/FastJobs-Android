package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.Participant
import com.rud.fastjobs.utils.toTimestamp
import java.time.LocalDateTime

class JobDao(private val firestoreInstance: FirebaseFirestore) {
    private val jobsRef = firestoreInstance.collection("jobs")
    private val usersRef = firestoreInstance.collection("users")

    fun getAllJobsLiveData(onComplete: (LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        val result = jobsRef.asLiveData<Job>()
        onComplete(result)
    }

    fun getAllCommentsLiveData(id: String, onComplete: (LiveData<FirestoreResource<List<Comment>>>) -> Unit) {
        val commentsRef = jobsRef.document(id).collection("comments")
        val result = commentsRef.asLiveData<Comment>()
        onComplete(result)
    }

    fun getAllParticipantsLiveData(id: String, onComplete: (LiveData<FirestoreResource<List<Participant>>>) -> Unit) {
        val participantsRef = jobsRef.document(id).collection("participants")
        val result = participantsRef.asLiveData<Participant>()
        onComplete(result)
    }

    fun addJob(job: Job, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.add(job).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }

    fun updateJob(id: String, jobFieldMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.document(id).update(jobFieldMap).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }

    fun deleteJob(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.document(id).delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }

    fun getJobById(id: String, onSuccess: (Job?) -> Unit, onFailure: (Exception) -> Unit) {
        jobsRef.document(id).get().addOnSuccessListener {
            val job = it.toObject(Job::class.java)
            job?.id = id
            onSuccess(job)
        }.addOnFailureListener(onFailure)
    }

    private fun getUserDocRef(userId: String) = usersRef.document(userId)

    fun joinJob(userId: String, jobId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val participant = Participant(getUserDocRef(userId), LocalDateTime.now().toTimestamp())
        jobsRef.document(jobId).collection("participants").add(participant)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener(onFailure)
    }

    fun leaveJob(
        userId: String,
        jobId: String,
        participantId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // unoptimized version
        val query = jobsRef.document(jobId).collection("participants")
            .whereEqualTo("user", getUserDocRef(userId))

        query.get().addOnSuccessListener { docs ->
            docs.first().reference.delete().addOnSuccessListener { onSuccess() }
        }

        // val docRef = jobsRef.document(jobId).collection("participants").document(participantId)
        // docRef.delete().addOnSuccessListener {
        //     onSuccess()
        // }.addOnFailureListener(onFailure)
    }

    fun postComment(
        userId: String,
        jobId: String,
        text: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val comment = Comment(getUserDocRef(userId), text, LocalDateTime.now().toTimestamp())
        jobsRef.document(jobId).collection("comments").add(comment)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener(onFailure)
    }

    fun deleteComment(
        jobId: String,
        commentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = jobsRef.document(jobId).collection("comments").document(commentId)
        docRef.delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }
}