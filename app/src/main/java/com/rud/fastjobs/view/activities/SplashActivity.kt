package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.rud.fastjobs.R
import timber.log.Timber

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        val sharedPref by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
        val allNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_all_notif), true)
        val jobsNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_jobs_notif), true)
        val memberNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_member_notif), true)
        val globalNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_global_notif), true)

        if (allNotifsEnabled) {
            // sub to all topics
            FirebaseMessaging.getInstance().subscribeToTopic("jobs")
            FirebaseMessaging.getInstance().subscribeToTopic("global")
            Timber.d("Device subscribed to all FCM topic")
        } else {
            if (jobsNotifsEnabled) {
                FirebaseMessaging.getInstance().subscribeToTopic("jobs")
                Timber.d("Device subscribed to FCM topic: jobs")
            }
            if (globalNotifsEnabled) {
                FirebaseMessaging.getInstance().subscribeToTopic("global")
                Timber.d("Device subscribed to FCM topic: global")
            }
        }

        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Timber.d(it.currentUser?.uid)
                val intent = Intent(this, JobDashboardActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
