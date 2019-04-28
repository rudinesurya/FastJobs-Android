package com.rud.fastjobs.viewmodel.jobDashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.repository.Store

class JobListViewModel(
    store: Store,
    private val auth: Auth,
    app: Application
) : AndroidViewModel(app) {
    val currentUser = auth.currentUserProfile
    val jobs = store.jobs
}