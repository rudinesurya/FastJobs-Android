package com.rud.fastjobs

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GooglePlacesQueryTest {
    lateinit var appContext: Context
    lateinit var apiKey: String

    @BeforeClass
    fun init() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        apiKey = appContext.getString(R.string.google_api_key)
    }

    @Test
    fun testGoogleApiKeyIsWorking() {

    }
}