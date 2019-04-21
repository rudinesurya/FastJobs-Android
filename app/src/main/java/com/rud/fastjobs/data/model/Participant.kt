package com.rud.fastjobs.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Participant(
    val user: DocumentReference? = null,
    val joinDate: Timestamp? = null
)