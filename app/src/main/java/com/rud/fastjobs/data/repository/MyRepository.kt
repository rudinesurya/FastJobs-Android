package com.rud.fastjobs.data.repository

import androidx.lifecycle.LiveData
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.rud.fastjobs.data.db.JobDao
import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.network.NearbyPlacesDataSource
import com.rud.fastjobs.data.network.response.NearbyPlacesResponse

class MyRepository(
    private val userDao: UserDao,
    private val jobDao: JobDao,
    private val nearbyPlacesDataSource: NearbyPlacesDataSource
) {
    // LiveData
    fun getUserByIdLiveData(id: String, onComplete: (LiveData<FirestoreResource<User>>) -> Unit = {}) {
        userDao.getUserByIdLiveData(id, onComplete)
    }

    fun getAllJobsLiveData(onComplete: (LiveData<FirestoreResource<List<Job>>>) -> Unit = {}) {
        jobDao.getAllJobsLiveData(onComplete)
    }

    // UserDao
    fun getUserById(id: String, onSuccess: (User?) -> Unit = {}) {
        userDao.getUserById(id, onSuccess)
    }

    fun addUser(id: String, user: User, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        userDao.addUser(id, user, onSuccess, onFailure)
    }

    fun updateUser(
        id: String,
        userFieldMap: Map<String, Any>,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        userDao.updateUser(id, userFieldMap, onSuccess, onFailure)
    }

    fun uploadAvatar(
        id: String,
        imageBytes: ByteArray,
        onSuccess: (String) -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        userDao.uploadAvatar(id, imageBytes, onSuccess, onFailure)
    }

    fun pathToReference(path: String) = userDao.pathToReference(path)

    // JobDao
    fun addJob(job: Job, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        jobDao.addJob(job, onSuccess, onFailure)
    }

    fun updateJob(
        id: String,
        jobFieldMap: Map<String, Any>,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        jobDao.updateJob(id, jobFieldMap, onSuccess, onFailure)
    }

    fun getJobById(id: String, onSuccess: (Job?) -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        jobDao.getJobById(id, onSuccess, onFailure)
    }

    fun joinJob(userId: String, jobId: String, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        jobDao.joinJob(userId, jobId, onSuccess, onFailure)
    }

    fun leaveJob(
        userId: String,
        jobId: String,
        participantId: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        jobDao.leaveJob(userId, jobId, participantId, onSuccess, onFailure)
    }

    fun postComment(
        userId: String,
        jobId: String,
        text: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        jobDao.postComment(userId, jobId, text, onSuccess, onFailure)
    }

    fun deleteComment(
        jobId: String,
        commentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        jobDao.deleteComment(jobId, commentId, onSuccess, onFailure)
    }

    // Nearby Places Data Source
    fun fetchNearbyPlaces(
        location: String,
        radius: String,
        type: String,
        onSuccess: (NearbyPlacesResponse) -> Unit = {},
        onFailure: (Throwable) -> Unit = {}
    ) {
        nearbyPlacesDataSource.fetchNearbyPlaces(location, radius, type, onSuccess, onFailure)
    }
}