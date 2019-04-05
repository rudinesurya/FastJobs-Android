package com.rud.fastjobs.data.model

import com.google.firebase.firestore.GeoPoint


data class Venue(
    val name: String = "",
    val address: String = "",
    val geoPoint: GeoPoint? = null
)