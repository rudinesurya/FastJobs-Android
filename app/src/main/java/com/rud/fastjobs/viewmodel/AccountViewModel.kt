package com.rud.fastjobs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.MyApplication
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.model.Venue
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.data.repository.Store

class AccountViewModel(
    private val myRepository: MyRepository,
    private val store: Store,
    private val auth: Auth,
    app: Application
) :
    AndroidViewModel(app) {
    private val app = getApplication<MyApplication>()
    val currentUser = auth.currentUserProfile
    var selectedImageBytes: ByteArray? = null
    var pictureJustChanged = false
    var currentSelectedVenue: Venue? = null

    fun getCurrentUserLiveData(onComplete: (LiveData<FirestoreResource<User>>) -> Unit = {}) {
        myRepository.getUserByIdLiveData(auth.currentUserProfile.value?.id!!, onComplete)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (String) -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        myRepository.uploadAvatar(auth.currentUserProfile.value?.id!!, imageBytes, onSuccess = {
            onSuccess(it)
        }, onFailure = {
            onFailure(it)
        })
    }

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun updateCurrentUser(userFieldMap: Map<String, Any>) {
        if (userFieldMap.count() == 0) {
            return Toast.makeText(getApplication(), "Nothing changes", Toast.LENGTH_SHORT).show()
        }

        myRepository.updateUser(auth.currentUserProfile.value?.id!!, userFieldMap, onSuccess = {
            Toast.makeText(app, "Saved!", Toast.LENGTH_SHORT).show()
        })
    }

    fun handleSave(displayName: String, bio: String) {
        val userFieldMap = mutableMapOf<String, Any>()

        if (displayName.isNotBlank() && displayName != currentUser.value?.name)
            userFieldMap["name"] = displayName
        if (bio != currentUser.value?.bio)
            userFieldMap["bio"] = bio
        if (currentSelectedVenue != null)
            userFieldMap["location"] = currentSelectedVenue!!

        // if no new image is selected, or
        // if image fails to be uploaded,
        // update user without uploading image to storage
        // else upload user data with the avatarurl to the storage path
        if (selectedImageBytes != null) {
            uploadAvatar(selectedImageBytes!!,
                onSuccess = { imagePath ->
                    if (imagePath != currentUser.value?.avatarUrl)
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