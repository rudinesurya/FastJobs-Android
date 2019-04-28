package com.rud.fastjobs.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import timber.log.Timber

/***
 * Class used to hold references to common objects to avoid multiple querying of the same data
 */
class Store(private val repository: MyRepository) {
    // LiveData
    private val _jobs = MutableLiveData<List<Job>>()
    val jobs: LiveData<List<Job>?>
        get() = _jobs

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>?>
        get() = _users

    init {
        fetchAllJobs()
        fetchAllUsers()
    }

    fun fetchAllJobs() {
        Timber.d("fetching All Jobs")
        repository.getAllJobsLiveData {
            it.observeForever {
                _jobs.postValue(it.data)
            }
        }
    }

    fun fetchAllUsers() {
        Timber.d("fetching All Users")
        repository.getAllUsersLiveData {
            it.observeForever {
                _users.postValue(it.data)
            }
        }
    }
}