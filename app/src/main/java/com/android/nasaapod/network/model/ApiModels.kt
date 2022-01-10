package com.android.nasaapod.network.model

data class PicOfTheDayResponse(
    val date: String,
    val title: String,
    val explanation: String,
    val hdurl: String,
    val url: String,
    val media_type: String,
    val service_version: String
)
