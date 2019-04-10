package com.rud.fastjobs

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.rud.fastjobs.view.activities.LoginActivity
import com.rud.fastjobs.view.activities.SignUpActivity
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test


class LogInActivityTest {
    companion object {
        lateinit var appContext: Context

        @BeforeClass
        @JvmStatic
        fun setup() {
            appContext = InstrumentationRegistry.getInstrumentation().targetContext

            Intents.init()
        }
    }

    @Rule
    @JvmField
    val rule1 = ActivityTestRule(LoginActivity::class.java)

    @Rule
    @JvmField
    val rule2 = IntentsTestRule(SignUpActivity::class.java, false, false)


    @Test
    fun testCanWriteEmail() {
        onView(withId(R.id.input_email)).perform(typeText("email 123"))
    }

    @Test
    fun testCanWritePassword() {
        onView(withId(R.id.input_password)).perform(typeText("password 123"))
    }

    @Test
    fun testLinkToSignUp() {
        onView(withId(R.id.link_signup)).perform(click())
        intended(hasComponent(SignUpActivity::class.java.name))
    }

    @Test
    fun testGoogleLogIn() {
        onView(withId(R.id.btn_googleLogin)).perform(click())
    }
}