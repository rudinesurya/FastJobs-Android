package com.rud.fastjobs.data.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.db.JobDao
import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User


class MyRepository(private val userDao: UserDao, private val jobDao: JobDao) {
    fun getCurrentUserLiveData(onComplete: (user: LiveData<FirestoreResource<User>>) -> Unit) {
        userDao.getCurrentUserLiveData(onComplete)
    }

    fun initCurrentUserIfNew(onSuccess: () -> Unit) {
        userDao.initCurrentUserIfNew(onSuccess)
    }

    fun updateCurrentUser(userFieldMap: Map<String, Any>) {
        userDao.updateCurrentUser(userFieldMap)
    }

    fun uploadAvatar(imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        userDao.uploadAvatar(imageBytes, onSuccess)
    }

    fun pathToReference(path: String) = userDao.pathToReference(path)

    fun getAllJobs(onComplete: (jobs: LiveData<FirestoreResource<List<Job>>>) -> Unit) {
        jobDao.getAllJobs(onComplete)
    }

    fun addJob(job: Job, onComplete: (Task<DocumentReference>) -> Unit) {
        jobDao.addJob(job, onComplete)
    }
}