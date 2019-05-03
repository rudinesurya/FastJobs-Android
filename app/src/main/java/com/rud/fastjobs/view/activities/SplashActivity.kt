package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().subscribeToTopic("jobs")
        Timber.d("Device subscribed to FCM topic: jobs")

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
