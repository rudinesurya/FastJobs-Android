package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.rud.fastjobs.R
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber


class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val myRepository: MyRepository by instance()
    private val auth: Auth by instance()
    private val RC_SIGNUP = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            login()
        }

        link_signup.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }
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

        val intent = Intent(this, MainActivity::class.java)
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

        if (requestCode == RC_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Timber.d("RESULT_OK")
                onLoginSuccess(auth.currentUser!!)
            }
        }
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