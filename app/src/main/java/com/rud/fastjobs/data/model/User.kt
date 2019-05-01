package com.rud.fastjobs.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel

data class User(
    val name: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val location: Venue? = null,
    val joinedList: MutableList<String> = mutableListOf(),
    val favList: MutableList<String> = mutableListOf(),
    val registerationToken: String = ""
) : FirestoreModel()