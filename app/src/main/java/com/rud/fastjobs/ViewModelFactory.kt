package com.rud.fastjobs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.viewmodel.AccountViewModel
import com.rud.fastjobs.viewmodel.JobDetailViewModel
import com.rud.fastjobs.viewmodel.JobListViewModel
import com.rud.fastjobs.viewmodel.JobRegistrationViewModel


class ViewModelFactory(private val myRepository: MyRepository, private val app: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @SuppressWarnings("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            AccountViewModel::class.java -> AccountViewModel(myRepository, app) as T
            JobListViewModel::class.java -> JobListViewModel(myRepository) as T
            JobRegistrationViewModel::class.java -> JobRegistrationViewModel(myRepository, app) as T
            JobDetailViewModel::class.java -> JobDetailViewModel(myRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}