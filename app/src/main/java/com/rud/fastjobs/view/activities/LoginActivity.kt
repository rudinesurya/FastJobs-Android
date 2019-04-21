package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.viewmodel.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: LoginActivityViewModel
    private val myRepository: MyRepository by instance()
    private val auth: Auth by instance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_GOOGLE_SIGN_IN = 0
    private val RC_SIGNUP = 1

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginActivityViewModel::class.java)

        btn_login.setOnClickListener {
            login()
        }

        link_signup.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_googleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        btn_fbLogin.setReadPermissions("email", "public_profile")
        btn_fbLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Timber.d("facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Timber.d("facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Timber.e(error)
            }
        })
    }

    fun login() {
        Timber.d("login")

        if (!validate()) {
            onLoginFailed()
            return
        }

        btn_login.isEnabled = false

        val email = input_email.text.toString()
        val password = input_password.text.toString()

        auth.signInWithEmailAndPassword(email, password, onSuccess = {
            onLoginSuccess(it)
        }, onFailure = {
            onLoginFailed()
        })
    }

    fun onLoginSuccess(user: FirebaseUser) {
        Timber.d("onLoginSuccess")
        btn_login.isEnabled = true
        initUserIfNew(user)

        val intent = Intent(this, JobDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    fun onLoginFailed() {
        Timber.d("onLoginFailed")
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
        btn_login.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true
        val email = input_email.text.toString()
        val password = input_password.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.error = "enter a valid email address"
            valid = false
        } else {
            input_email.error = null
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            input_password.error = null
        }

        return valid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGNUP -> {
                if (resultCode == RESULT_OK) {
                    Timber.d("RESULT_OK")
                    onLoginSuccess(auth.currentUser!!)
                }
            }

            RC_GOOGLE_SIGN_IN -> {
                if (resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account!!)
                    } catch (e: ApiException) {
                        // Google Sign In failed
                        Timber.e(e)
                    }
                }
            }

            else -> callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.d("firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential, onSuccess = {
            onLoginSuccess(it)
        }, onFailure = {
            onLoginFailed()
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Timber.d("handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential, onSuccess = {
            onLoginSuccess(it)
        }, onFailure = {
            onLoginFailed()
        })
    }

    fun initUserIfNew(user: FirebaseUser) {
        myRepository.getUserById(user.uid, onSuccess = {
            if (it == null) {
                val newUser = User(
                    name = user.displayName ?: "John Doe",
                    email = user.email ?: "johndoe@gmail.com",
                    bio = "",
                    avatarUrl = null
                )

                myRepository.addUser(user.uid, newUser)
            }
        })
    }
}