package com.rud.fastjobs.data.model

import com.google.firebase.Timestamp
import com.ptrbrynt.firestorelivedata.FirestoreModel

data class Participant(
    val userName: String = "",
    val userAvatarUrl: String = "",
    val joinDate: Timestamp? = null
) : FirestoreModel()