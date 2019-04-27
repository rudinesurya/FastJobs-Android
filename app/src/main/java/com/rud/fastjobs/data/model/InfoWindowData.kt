package com.rud.fastjobs.data.model

data class InfoWindowData(
    val locationName: String = "",
    val locationAddress: String = "",
    val locationHours: String = "",
    val job: Job? = null
)