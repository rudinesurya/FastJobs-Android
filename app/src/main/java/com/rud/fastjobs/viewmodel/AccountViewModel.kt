package com.rud.fastjobs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository


class AccountViewModel(private val myRepository: MyRepository) : ViewModel() {
    var selectedImageBytes: ByteArray? = null
    var pictureJustChanged = false

    fun getCurrentUser(onSuccess: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        myRepository.getCurrentUser(onSuccess)
    }

    fun updateCurrentUser(name: String = "", bio: String = "", avatarUrl: String? = null) {
        myRepository.updateCurrentUser(name, bio, avatarUrl)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        myRepository.uploadAvatar(imageBytes, onSuccess)
    }

    fun pathToReference(path: String) = myRepository.pathToReference(path)

    fun handleSave(displayName: String, bio: String) {
        if (selectedImageBytes != null) {
            uploadAvatar(selectedImageBytes!!) { imagePath ->
                updateCurrentUser(displayName, bio, imagePath)
            }
        } else {
            updateCurrentUser(displayName, bio)
        }
    }
}

class AccountViewModelFactory(private val myRepository: MyRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @SuppressWarnings("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountViewModel(myRepository) as T
    }
}