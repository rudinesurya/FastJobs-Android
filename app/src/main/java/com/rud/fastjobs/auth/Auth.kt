package com.rud.fastjobs.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Auth(private val firebaseAuth: FirebaseAuth) {
    val currentUser
        get() = firebaseAuth.currentUser

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccess(it.user)
        }.addOnFailureListener { onFailure(it) }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            onSuccess(it.user)
        }.addOnFailureListener { onFailure(it) }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}