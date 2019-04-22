package com.rud.fastjobs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.viewmodel.AccountViewModel
import com.rud.fastjobs.viewmodel.JobRegistrationViewModel
import com.rud.fastjobs.viewmodel.LoginActivityViewModel
import com.rud.fastjobs.viewmodel.MapsActivityViewModel
import com.rud.fastjobs.viewmodel.SignUpActivityViewModel
import com.rud.fastjobs.viewmodel.jobDashboard.JobDashboardActivityViewModel
import com.rud.fastjobs.viewmodel.jobDashboard.JobListViewModel
import com.rud.fastjobs.viewmodel.jobDetail.ChatRoomViewModel
import com.rud.fastjobs.viewmodel.jobDetail.JobDetailViewModel
import com.rud.fastjobs.viewmodel.jobDetail.ParticipantListViewModel

class ViewModelFactory(
    private val myRepository: MyRepository,
    private val auth: Auth,
    private val app: Application
) :
    ViewModelProvider.NewInstanceFactory() {

    @SuppressWarnings("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {

            AccountViewModel::class.java -> AccountViewModel(myRepository, auth, app) as T
            JobListViewModel::class.java -> JobListViewModel(
                myRepository,
                app
            ) as T
            JobRegistrationViewModel::class.java -> JobRegistrationViewModel(myRepository, app) as T
            MapsActivityViewModel::class.java -> MapsActivityViewModel(myRepository, app) as T
            JobDetailViewModel::class.java -> JobDetailViewModel(
                myRepository,
                app
            ) as T
            JobDashboardActivityViewModel::class.java -> JobDashboardActivityViewModel(
                myRepository,
                app
            ) as T
            LoginActivityViewModel::class.java -> LoginActivityViewModel(myRepository, app) as T
            SignUpActivityViewModel::class.java -> SignUpActivityViewModel(myRepository, app) as T
            ChatRoomViewModel::class.java -> ChatRoomViewModel(
                myRepository,
                app
            ) as T
            ParticipantListViewModel::class.java -> ParticipantListViewModel(
                myRepository,
                app
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}