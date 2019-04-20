package com.rud.fastjobs.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel

data class User(
    val name: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUrl: String? = null
) : FirestoreModel()