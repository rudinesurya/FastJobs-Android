package com.rud.fastjobs.data.model

import com.google.firebase.Timestamp
import com.ptrbrynt.firestorelivedata.FirestoreModel

data class Comment(
    val userName: String = "",
    val userAvatarUrl: String = "",
    val text: String = "",
    val postDate: Timestamp? = null
) : FirestoreModel()