package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.rud.fastjobs.data.model.User
import java.util.*


class UserDao(
    private val firestoreInstance: FirebaseFirestore,
    private val storageInstance: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) {
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${firebaseAuth.currentUser?.uid!!}")
    private val currentUserStorageRef: StorageReference
        get() = storageInstance.reference.child(firebaseAuth.currentUser?.uid!!)


    fun getCurrentUserLiveData(onComplete: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        val result = currentUserDocRef.asLiveData<User>()
        onComplete(result)
    }

    fun initCurrentUserIfNew(onSuccess: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            if (!it.exists()) {
                val currentUser = firebaseAuth.currentUser
                val newUser = User(
                    name = currentUser?.displayName ?: "John Doe",
                    bio = "",
                    avatarUrl = null
                )

                currentUserDocRef.set(newUser).addOnSuccessListener { onSuccess() }
            } else
                onSuccess()
        }
    }

    fun updateCurrentUser(userFieldMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        currentUserDocRef.update(userFieldMap).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val ref = currentUserStorageRef.child("avatars/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes).addOnSuccessListener {
            onSuccess(ref.path)
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun pathToReference(path: String) = storageInstance.getReference(path)
}