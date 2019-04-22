package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository

class SignUpActivityViewModel(
    private val myRepository: MyRepository, app: Application
) : AndroidViewModel(app) {
    fun initUserIfNew(user: FirebaseUser, name: String) {
        myRepository.getUserById(user.uid, onSuccess = {
            if (it == null) {
                val newUser = User(
                    name = name,
                    email = user.email!!,
                    bio = ""
                )

                myRepository.addUser(user.uid, newUser)
            }
        })
    }
}