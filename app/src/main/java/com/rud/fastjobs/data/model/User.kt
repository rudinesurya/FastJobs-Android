package com.rud.fastjobs.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel


data class User(
    val name: String,
    val bio: String,
    val avatarUrl: String?
) : FirestoreModel() {
    constructor() : this("", "", null)
}