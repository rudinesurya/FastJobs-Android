package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.User

/***
 * User data access object
 */
class UserDao(
    private val firestoreInstance: FirebaseFirestore
) {
    private val usersRef = firestoreInstance.collection("users")

    fun getUserByIdLiveData(id: String, onComplete: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        val result = usersRef.document(id).asLiveData<User>()
        onComplete(result)
    }

    fun getAllUsersLiveData(onComplete: (LiveData<FirestoreResource<List<User>>>) -> Unit) {
        val result = usersRef.asLiveData<User>()
        onComplete(result)
    }

    fun getUserById(id: String, onSuccess: (User?) -> Unit) {
        usersRef.document(id).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            user?.id = it.id
            onSuccess(user)
        }
    }

    fun addUser(id: String, user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.document(id).set(user).addOnSuccessListener { onSuccess() }.addOnFailureListener(onFailure)
    }

    fun updateUser(id: String, userFieldMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.document(id).update(userFieldMap).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }

    fun addJob(userId: String, jobId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        usersRef.document(userId).update("joinedList", FieldValue.arrayUnion(jobId)).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }

    fun deleteJob(userId: String, jobId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        usersRef.document(userId).update("joinedList", FieldValue.arrayRemove(jobId)).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }

    fun addFav(userId: String, jobId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        usersRef.document(userId).update("favList", FieldValue.arrayUnion(jobId)).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }

    fun deleteFav(userId: String, jobId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        usersRef.document(userId).update("favList", FieldValue.arrayRemove(jobId)).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener(onFailure)
    }
}