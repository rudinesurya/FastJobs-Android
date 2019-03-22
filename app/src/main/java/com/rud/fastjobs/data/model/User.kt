package com.rud.fastjobs.data.model


data class User(
    val name: String,
    val bio: String,
    val avatarUrl: String?
) {
    constructor() : this("", "", null)
}