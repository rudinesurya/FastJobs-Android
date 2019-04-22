package com.rud.fastjobs.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Auth(private val firebaseAuth: FirebaseAuth) {
    val currentUser = firebaseAuth.currentUser!!

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