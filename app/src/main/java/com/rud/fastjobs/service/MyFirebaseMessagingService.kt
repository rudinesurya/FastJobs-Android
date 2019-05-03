package com.rud.fastjobs.service

import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rud.fastjobs.R
import timber.log.Timber

/***
 * Class that will handle firebase cloud messages
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    val sharedPref by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val allNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_all_notif), true)
        Timber.d("allNotifsEnabled: $allNotifsEnabled")
        if (allNotifsEnabled) {

        } else {
            val jobsNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_jobs_notif), true)
            Timber.d("jobsNotifsEnabled: $jobsNotifsEnabled")
            if (jobsNotifsEnabled) {

                // Subscribe to jobs topic
            }
        }

        remoteMessage?.let {
            // val message = it.data.get("message")
            val title = it.notification?.title
            Timber.d("title: $title")
        }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Timber.d("token: $newToken")


        sharedPref.edit {
            putString("registerationToken", newToken)
        }
    }
}
