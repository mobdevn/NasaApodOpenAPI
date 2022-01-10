package com.android.nasaapod.network.repository

import com.android.nasaapod.network.model.PicOfTheDayResponse

interface ApiRepositoryInterface {
    suspend fun getPicOfTheDay(date: String) : Result<PicOfTheDayResponse>
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
