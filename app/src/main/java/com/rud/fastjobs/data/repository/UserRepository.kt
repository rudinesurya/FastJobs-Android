package com.rud.fastjobs.data.repository

import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.model.User


class UserRepository(private val userDao: UserDao) {
    fun getCurrentUser(onComplete: (User) -> Unit) {
        userDao.getCurrentUser(onComplete)
    }

    fun initCurrentUserIfNew(onComplete: () -> Unit) {
        userDao.initCurrentUserIfNew(onComplete)
    }

    fun updateCurrentUser(name: String = "", bio: String = "", avatarUrl: String? = null) {
        userDao.updateCurrentUser(name, bio, avatarUrl)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        userDao.uploadAvatar(imageBytes, onSuccess)
    }

    fun pathToReference(path: String) = userDao.pathToReference(path)
}