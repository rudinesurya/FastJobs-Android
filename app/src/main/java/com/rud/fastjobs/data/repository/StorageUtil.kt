package com.rud.fastjobs.data.repository

import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

/***
 * Class made for firestore storage access
 */
class StorageUtil(private val storageInstance: FirebaseStorage) {
    private val storageRef = storageInstance.reference.child("users")

    fun pathToReference(path: String) = storageInstance.getReference(path)

    fun uploadAvatar(id: String, imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val ref = storageRef.child(id).child("avatars/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes).addOnSuccessListener {
            onSuccess(ref.path)
        }.addOnFailureListener {
            onFailure(it)
        }
    }
}
