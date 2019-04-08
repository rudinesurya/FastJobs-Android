package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.User
import java.util.*


class UserDao(
    private val firestoreInstance: FirebaseFirestore,
    private val storageInstance: FirebaseStorage
) {
    private val usersRef = firestoreInstance.collection("users")
    private val storageRef = storageInstance.reference.child("users")

    fun getUserByIdLiveData(id: String, onComplete: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        val result = usersRef.document(id).asLiveData<User>()
        onComplete(result)
    }

    fun getUserById(id: String, onSuccess: (User?) -> Unit) {
        usersRef.document(id).get().addOnSuccessListener {
            onSuccess(it.toObject(User::class.java))
        }
    }

    fun addUser(id: String, user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.document(id).set(user).addOnSuccessListener { onSuccess() }.addOnFailureListener(onFailure)
    }

    fun updateUser(id: String, userFieldMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.document(id).update(userFieldMap).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun uploadAvatar(id: String, imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val ref = storageRef.child(id).child("avatars/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes).addOnSuccessListener {
            onSuccess(ref.path)
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun pathToReference(path: String) = storageInstance.getReference(path)
}