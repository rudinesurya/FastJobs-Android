package com.rud.fastjobs.data.model

import com.google.firebase.Timestamp
import com.ptrbrynt.firestorelivedata.FirestoreModel

data class Job(
    val title: String = "",
    val hostName: String = "",
    val hostUid: String = "",
    val hostAvatarUrl: String = "",
    val description: String = "",
    val payout: Double = 0.0,
    val urgency: Boolean = false,
    val venue: Venue? = null,
    val date: Timestamp? = null,
    val status: Boolean = true,
    val photoUrls: MutableList<String> = mutableListOf()
) : FirestoreModel()