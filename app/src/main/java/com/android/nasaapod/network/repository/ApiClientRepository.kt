package com.android.nasaapod.network.repository

import com.android.nasaapod.network.model.PicOfTheDayResponse

class ApiClientRepository : ApiRepositoryInterface {
    private val url = "https://api.nasa.gov/planetary/apod?api_key="

    override suspend fun getPicOfTheDay(date: String): Result<PicOfTheDayResponse> {
        TODO("Not yet implemented")
    }
}
