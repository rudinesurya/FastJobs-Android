package com.rud.fastjobs.data.model

import com.ptrbrynt.firestorelivedata.FirestoreModel


data class Job(
    val title: String,
    val hostName: String,
    val hostUid: String,
    val hostAvatarUrl: String,
    val description: String,
    val payout: Double,
    val urgency: Boolean
) : FirestoreModel() {
    constructor() : this("", "", "", "", "", 0.0, false)
}