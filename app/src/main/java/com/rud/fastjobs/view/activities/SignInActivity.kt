package com.rud.fastjobs.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.rud.fastjobs.R
import com.rud.fastjobs.data.repository.UserRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber


class SignInActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val userRepository: UserRepository by instance()
    private val RC_SIGN_IN = 1;

    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
//        AuthUI.IdpConfig.PhoneBuilder().build(),
//        AuthUI.IdpConfig.GoogleBuilder().build(),
//        AuthUI.IdpConfig.FacebookBuilder().build()
//        AuthUI.IdpConfig.TwitterBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                userRepository.initCurrentUserIfNew {
                    Timber.d("Successfully signed in")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                if (response != null) {
                    when (response.error?.errorCode) {
                        ErrorCodes.NO_NETWORK -> Timber.e("No Network")
                        else -> Timber.e("Unknown Error")
                    }
                } else {
                    Timber.e("User cancelled the sign in")
                }
            }
        }
    }
}
