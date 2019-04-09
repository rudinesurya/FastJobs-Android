package com.rud.fastjobs.data.network.response

import com.google.gson.annotations.SerializedName

data class NearbyPlacesResponse(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    val nextPageToken: String,
    val results: List<Result>,
    val status: String
)