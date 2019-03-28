package com.rud.fastjobs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository


class AccountViewModel(private val myRepository: MyRepository) : ViewModel() {
    lateinit var currentUser: User
    var selectedImageBytes: ByteArray? = null
    var pictureJustChanged = false

    fun getCurrentUserLiveData(onComplete: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        myRepository.getCurrentUserLiveData(onComplete)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        myRepository.uploadAvatar(imageBytes, onSuccess)
    }

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun handleSave(displayName: String, bio: String) {
        val userFieldMap = mutableMapOf<String, Any>()

        if (displayName.isNotBlank() && displayName != currentUser.name)
            userFieldMap["name"] = displayName
        if (bio != currentUser.bio)
            userFieldMap["bio"] = bio

        if (selectedImageBytes != null) {
            uploadAvatar(selectedImageBytes!!) { imagePath ->
                userFieldMap["avatarUrl"] = imagePath
            }
        }
        myRepository.updateCurrentUser(userFieldMap)
    }
}