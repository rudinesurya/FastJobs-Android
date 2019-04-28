package com.rud.fastjobs.service

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

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Timber.d("token: $token")
        sendRegistrationToServer(token)
    }

    fun sendRegistrationToServer(token: String?) {
    }
}
