package com.rud.fastjobs.view.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.google.firebase.messaging.FirebaseMessaging
import com.rud.fastjobs.R
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {
    val sharedPref by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        val key = preference?.key
        val value = sharedPref.getBoolean(key, true)

        // Timber.d("preference [$key] changed to [$value]")

        when (key) {
            getString(R.string.pref_enable_all_notif) -> {
                if (value) {
                    FirebaseMessaging.getInstance().subscribeToTopic("jobs")
                    FirebaseMessaging.getInstance().subscribeToTopic("global")
                    Timber.d("Device subscribed to all FCM topic")
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jobs")
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("global")
                    Timber.d("Device unsubscribed from all FCM topic")
                }

                (findPreference(getString(R.string.pref_enable_jobs_notif)) as SwitchPreference).isChecked = value
                (findPreference(getString(R.string.pref_enable_member_notif)) as SwitchPreference).isChecked = value
                (findPreference(getString(R.string.pref_enable_global_notif)) as SwitchPreference).isChecked = value

                sharedPref.edit {
                    putBoolean(getString(R.string.pref_enable_jobs_notif), value)
                    putBoolean(getString(R.string.pref_enable_member_notif), value)
                    putBoolean(getString(R.string.pref_enable_global_notif), value)
                }
            }
            getString(R.string.pref_enable_jobs_notif) -> {
                if (value) {
                    FirebaseMessaging.getInstance().subscribeToTopic("jobs")
                    Timber.d("Device subscribed to FCM topic: jobs")
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jobs")
                    Timber.d("Device unsubscribed from FCM topic: jobs")
                }
            }
            getString(R.string.pref_enable_member_notif) -> {
                val word: String = if (value) "accepting" else "rejecting"
                Timber.d("Device $word token notifications")
            }
            getString(R.string.pref_enable_global_notif) -> {
                if (value) {
                    FirebaseMessaging.getInstance().subscribeToTopic("global")
                    Timber.d("Device subscribed to FCM topic: global")
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("global")
                    Timber.d("Device unsubscribed to FCM topic: global")
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}
