package com.rud.fastjobs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.UserRepository


class AccountViewModel(private val userRepository: UserRepository) : ViewModel() {
    var selectedImageBytes: ByteArray? = null
    var pictureJustChanged = false

    fun getCurrentUser(onSuccess: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        userRepository.getCurrentUser(onSuccess)
    }

    fun updateCurrentUser(name: String = "", bio: String = "", avatarUrl: String? = null) {
        userRepository.updateCurrentUser(name, bio, avatarUrl)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        userRepository.uploadAvatar(imageBytes, onSuccess)
    }

    fun pathToReference(path: String) = userRepository.pathToReference(path)

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

class AccountViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @SuppressWarnings("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountViewModel(userRepository) as T
    }
}