package com.rud.fastjobs.data.repository

import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.model.User


class UserRepository(private val userDao: UserDao) {
    fun getCurrentUser(onSuccess: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        userDao.getCurrentUser(onSuccess)
    }

    fun initCurrentUserIfNew(onSuccess: () -> Unit) {
        userDao.initCurrentUserIfNew(onSuccess)
    }

    fun updateCurrentUser(name: String = "", bio: String = "", avatarUrl: String? = null) {
        userDao.updateCurrentUser(name, bio, avatarUrl)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        userDao.uploadAvatar(imageBytes, onSuccess)
    }

    fun pathToReference(path: String) = userDao.pathToReference(path)
}