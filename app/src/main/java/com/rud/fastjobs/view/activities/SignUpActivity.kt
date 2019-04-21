package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.viewmodel.SignUpActivityViewModel
import kotlinx.android.synthetic.main.activity_signup.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SignUpActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: SignUpActivityViewModel
    private val auth: Auth by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SignUpActivityViewModel::class.java)

        btn_signup.setOnClickListener {
            signup()
        }

        link_login.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signup() {
        Timber.d("signup")

        if (!validate()) {
            onSignUpFailed()
            return
        }

        btn_signup.isEnabled = false

        val email = input_email.text.toString()
        val password = input_password.text.toString()

        auth.createUserWithEmailAndPassword(email, password, onSuccess = {
            onSignUpSuccess(it)
        }, onFailure = {
            onSignUpFailed()
        })
    }

    fun onSignUpSuccess(user: FirebaseUser) {
        Timber.d("onSignUpSuccess")

        viewModel.initUserIfNew(user, name = input_name.text.toString())
        btn_signup.isEnabled = true
        setResult(RESULT_OK, null)
        finish()
    }

    fun onSignUpFailed() {
        Timber.d("onSignUpFailed")
        Toast.makeText(this, "SignUp failed", Toast.LENGTH_LONG).show()
        btn_signup.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val name = input_name.text.toString()
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        val reEnterPassword = input_reEnterPassword.text.toString()

        if (name.isEmpty() || name.length < 3) {
            input_name.error = "at least 3 characters"
            valid = false
        } else {
            input_name.error = null
        }

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

        if (reEnterPassword.isEmpty() || reEnterPassword.length < 4 || reEnterPassword.length > 10 || reEnterPassword != password) {
            input_reEnterPassword.error = "Password Do not match"
            valid = false
        } else {
            input_reEnterPassword.error = null
        }

        return valid
    }
}