package com.android.nasaapod.network.repository

import android.graphics.Bitmap
import com.android.nasaapod.network.model.PicOfTheDayResponse

interface ApiRepositoryInterface {
    suspend fun getPicOfTheDayData(date: String) : Result<PicOfTheDayResponse>

    suspend fun getPicOfTheDay(url: String) : Bitmap?
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
