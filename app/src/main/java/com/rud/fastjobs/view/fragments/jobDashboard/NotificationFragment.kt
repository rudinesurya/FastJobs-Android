package com.rud.fastjobs.view.fragments.jobDashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rud.fastjobs.R
import com.rud.fastjobs.utils.FragmentLifecycle

class NotificationFragment : Fragment(), FragmentLifecycle {
    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }
}
