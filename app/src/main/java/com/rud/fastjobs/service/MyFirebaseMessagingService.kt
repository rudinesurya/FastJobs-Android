package com.rud.fastjobs.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.Notification
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.view.activities.JobDetailActivity
import com.rud.fastjobs.view.activities.SplashActivity
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
    val CHANNEL_ID = "FastJobsChannelId"
    val NOTIFICATION_ID = 101

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        val memberNotifsEnabled = sharedPref.getBoolean(getString(R.string.pref_enable_member_notif), true)

        remoteMessage?.let {
            // val message = it.data.get("message")
            val title = it.data["title"] ?: "na"
            val message = it.data["message"] ?: "na"
            val type = it.data["type"] ?: "na"
            val jobId = it.data["jobId"] ?: ""

            if (type == getString(R.string.pref_enable_member_notif) && !memberNotifsEnabled) {
                Timber.d("$type is disabled. ignoring this notification")
                return
            }

            var pendingIntent: PendingIntent? = null
            if (type == getString(R.string.pref_enable_member_notif) ||
                type == getString(R.string.pref_enable_jobs_notif)
            ) {
                Timber.d("received notification with jobId: $jobId")
                // Create an explicit intent for an Activity in your app
                val intent = Intent(this, JobDetailActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("id", jobId)
                }
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            } else {
                val intent = Intent(this, SplashActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            createNotificationChannel()
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_work_24px)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(NOTIFICATION_ID, builder.build())
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "FastJobsChannel"
            val descriptionText = "FastJobsChannel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
