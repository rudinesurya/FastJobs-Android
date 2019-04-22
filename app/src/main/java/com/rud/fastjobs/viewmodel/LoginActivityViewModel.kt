package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.rud.fastjobs.MyApplication
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository

class LoginActivityViewModel(
    private val myRepository: MyRepository, app: Application
) : AndroidViewModel(app) {
    private val app = getApplication<MyApplication>()
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var callbackManager: CallbackManager // Facebook

    fun getUserById(id: String, onSuccess: (User?) -> Unit = {}) {
        myRepository.getUserById(id, onSuccess)
    }

    fun addUser(id: String, user: User, onSuccess: () -> Unit = {}, onFailure: (Exception) -> Unit = {}) {
        myRepository.addUser(id, user, onSuccess, onFailure)
    }

    fun initUserIfNew(user: FirebaseUser) {
        myRepository.getUserById(user.uid, onSuccess = {
            if (it == null) {
                val newUser = User(
                    name = user.displayName ?: "John Doe",
                    email = user.email ?: "johndoe@gmail.com",
                    bio = ""
                )

                addUser(user.uid, newUser)
            }
        })
    }

    fun initGoogleSignin() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(app, gso)
    }

    fun initFacebookSignin() {
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
    }
}