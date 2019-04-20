package com.rud.fastjobs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository

class AccountViewModel(private val myRepository: MyRepository, private val auth: Auth, app: Application) :
    AndroidViewModel(app) {
    lateinit var currentUser: User
    var selectedImageBytes: ByteArray? = null
    var pictureJustChanged = false

    fun getCurrentUserLiveData(onComplete: (LiveData<FirestoreResource<User>>) -> Unit = {}) {
        myRepository.getUserByIdLiveData(auth.currentUser?.uid!!, onComplete)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (String) -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        myRepository.uploadAvatar(auth.currentUser?.uid!!, imageBytes, onSuccess = {
            onSuccess(it)
        }, onFailure = {
            onFailure(it)
        })
    }

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun updateCurrentUser(userFieldMap: Map<String, Any>) {
        myRepository.updateUser(auth.currentUser?.uid!!, userFieldMap, onSuccess = {
            Toast.makeText(getApplication(), "Saved!", Toast.LENGTH_SHORT).show()
        })
    }

    fun handleSave(displayName: String, bio: String) {
        val userFieldMap = mutableMapOf<String, Any>()

        if (displayName.isNotBlank() && displayName != currentUser.name)
            userFieldMap["name"] = displayName
        if (bio != currentUser.bio)
            userFieldMap["bio"] = bio

        // if no new image is selected, or
        // if image fails to be uploaded,
        // update user without uploading image to storage
        // else upload user data with the avatarurl to the storage path
        if (selectedImageBytes != null) {
            uploadAvatar(selectedImageBytes!!,
                onSuccess = { imagePath ->
                    userFieldMap["avatarUrl"] = imagePath
                    updateCurrentUser(userFieldMap)
                },
                onFailure = {
                    updateCurrentUser(userFieldMap)
                })
        } else
            updateCurrentUser(userFieldMap)
    }
}