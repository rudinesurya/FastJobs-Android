package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kiwimob.firestore.livedata.livedata
import com.rud.fastjobs.data.model.User
import java.util.*


class UserDao {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid!!}")
    private val currentUserStorageRef: StorageReference
        get() = storageInstance.reference.child(FirebaseAuth.getInstance().currentUser?.uid!!)


    fun getCurrentUserLiveData(): LiveData<User> {
        return currentUserDocRef.livedata(User::class.java)
    }

    fun initCurrentUserIfNew(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            if (!it.exists()) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val newUser = User(
                    currentUser?.displayName ?: "John Doe",
                    "",
                    null
                )

                currentUserDocRef.set(newUser).addOnSuccessListener { onComplete() }
            } else
                onComplete()
        }
    }

    fun updateCurrentUser(name: String = "", bio: String = "", avatarUrl: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()

        if (name.isNotBlank())
            userFieldMap["name"] = name
        if (bio.isNotBlank())
            userFieldMap["bio"] = bio

        avatarUrl?.let { userFieldMap["avatarUrl"] = it }
        currentUserDocRef.update(userFieldMap)
    }


    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        val ref = currentUserStorageRef.child("avatars/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes).addOnSuccessListener {
            onSuccess(ref.path)
        }
    }

    fun pathToReference(path: String) = storageInstance.getReference(path)
}