package com.rud.fastjobs.service

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

/***
 * Class that will handle firebase cloud messages
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        remoteMessage?.let {
            val message = it.data.get("message")
            Timber.d("message: $message")
        }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Timber.d("token: $newToken")
        val sharedPref: SharedPreferences = getSharedPreferences("Global", Context.MODE_PRIVATE)
        sharedPref.edit {
            putString("registerationToken", newToken)
        }
    }
}
