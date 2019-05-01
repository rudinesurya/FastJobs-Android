package com.rud.fastjobs.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rud.fastjobs.data.db.UserDao
import com.rud.fastjobs.data.model.User
import timber.log.Timber

class Auth(private val firebaseAuth: FirebaseAuth, private val userDao: UserDao) {
    private val _currentUserProfile = MutableLiveData<User>()
    val currentUserProfile: LiveData<User?>
        get() = _currentUserProfile

    // Idealy to be called after logging in
    fun fetchUserProfile(id: String) {
        userDao.getUserByIdLiveData(id) {
            it.observeForever {
                _currentUserProfile.postValue(it.data)
            }
        }
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser != null) {
                Timber.d("firebase auth user changed. ${it.currentUser?.uid}")
                fetchUserProfile(it.currentUser?.uid!!)
            }
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccess(it.user)
        }.addOnFailureListener(onFailure)
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccess(it.user)
        }.addOnFailureListener(onFailure)
    }

    fun signInWithCredential(
        credential: AuthCredential,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener { onSuccess(it.user) }
            .addOnFailureListener(onFailure)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}