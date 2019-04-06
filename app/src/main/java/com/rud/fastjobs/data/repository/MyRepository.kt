package com.rud.fastjobs.data.repository

import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.db.JobDao
import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User


class MyRepository(private val userDao: UserDao, private val jobDao: JobDao) {
    fun getCurrentUserLiveData(onComplete: (LiveData<FirestoreResource<User>>) -> Unit) {
        userDao.getCurrentUserLiveData(onComplete)
    }

    fun getCurrentUser(onSuccess: (User?) -> Unit) {
        userDao.getCurrentUser(onSuccess)
    }

    fun initCurrentUserIfNew(onSuccess: () -> Unit) {
        userDao.initCurrentUserIfNew(onSuccess)
    }

    fun updateCurrentUser(userFieldMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userDao.updateCurrentUser(userFieldMap, onSuccess, onFailure)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        userDao.uploadAvatar(imageBytes, onSuccess, onFailure)
    }

    fun pathToReference(path: String) = userDao.pathToReference(path)

    fun getAllJobsLiveData(onComplete: (LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        jobDao.getAllJobsLiveData(onComplete)
    }

    fun addJob(job: Job, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobDao.addJob(job, onSuccess, onFailure)
    }

    fun updateJob(id: String, jobFieldMap: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        jobDao.updateJob(id, jobFieldMap, onSuccess, onFailure)
    }

    fun getJobById(id: String, onSuccess: (Job?) -> Unit, onFailure: (Exception) -> Unit) {
        jobDao.getJobById(id, onSuccess, onFailure)
    }
}