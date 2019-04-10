package com.rud.fastjobs

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.rud.fastjobs.view.activities.MainActivity
import com.rud.fastjobs.view.fragments.JobDetailFragment
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test


class JobDetailTest {
    companion object {
        lateinit var appContext: Context

        @BeforeClass
        @JvmStatic
        fun setup() {
            appContext = InstrumentationRegistry.getInstrumentation().targetContext
        }
    }

    @Before
    fun before() {
        Intents.init()
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(ActivityResult(Activity.RESULT_OK, null))

        activityTestRule.activity.supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, JobDetailFragment()).commit()
    }

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testOpenGoogleMaps() {
        val gmmIntentUri = Uri.parse("geo:-33.8670522,151.1957362?z=12")

        onView(withId(R.id.mapView)).perform(ViewActions.click())
        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                toPackage("com.google.android.apps.maps")
            )
        )
    }
}