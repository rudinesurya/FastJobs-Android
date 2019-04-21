package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.data.repository.MyRepository

class JobDashboardActivityViewModel(
    private val myRepository: MyRepository, app: Application
) : AndroidViewModel(app) {

}