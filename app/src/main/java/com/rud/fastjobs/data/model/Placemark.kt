package com.rud.fastjobs.data.model

data class Placemark(
    val name: String,
    val lat: Double,
    val lng: Double,
    val job: Job? = null
)