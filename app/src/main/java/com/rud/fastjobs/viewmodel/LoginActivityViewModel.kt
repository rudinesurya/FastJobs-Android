package com.rud.fastjobs.viewmodel

import android.app.Application
import android.content.Context
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
import timber.log.Timber

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
        val token = app.getSharedPreferences("Global", Context.MODE_PRIVATE).getString("registerationToken", "")
        Timber.d(token)
        myRepository.getUserById(user.uid, onSuccess = {
            if (it == null) {
                val newUser = User(
                    name = user.displayName ?: "John Doe",
                    email = user.email ?: "johndoe@gmail.com",
                    bio = "",
                    registerationToken = token
                )

                addUser(user.uid, newUser)
            } else {
                val userFieldMap = mutableMapOf<String, Any>()
                userFieldMap["registerationToken"] = token
                myRepository.updateUser(it.id!!, userFieldMap)
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