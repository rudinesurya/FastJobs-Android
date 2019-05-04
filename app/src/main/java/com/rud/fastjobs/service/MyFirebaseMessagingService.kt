package com.rud.fastjobs.service

import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.Notification
import com.rud.fastjobs.data.repository.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

/***
 * Class that will handle firebase cloud messages
 */
class MyFirebaseMessagingService : FirebaseMessagingService(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    val myRepository: MyRepository by instance()
    val sharedPref by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        val allNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_all_notif), true)
        val memberNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_member_notif), true)

        remoteMessage?.let {
            // val message = it.data.get("message")
            val title = it.data["title"] ?: "na"
            val message = it.data["message"] ?: "na"
            val type = it.data["type"] ?: "na"

            if (type == getString(R.string.pref_enable_member_notif) && !memberNotifsEnabled) {
                Timber.d("$type is disabled. ignoring this notification")
                return
            }

            GlobalScope.launch(Dispatchers.Main) {
                myRepository.addNotification(Notification(title = title, message = message))
            }
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
