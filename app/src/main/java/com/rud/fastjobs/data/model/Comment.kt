package com.rud.fastjobs.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Comment(
    val user: DocumentReference? = null,
    val text: String = "",
    val postDate: Timestamp? = null
)