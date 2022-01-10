package com.android.nasaapod.network.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.android.nasaapod.BuildConfig
import com.android.nasaapod.network.model.PicOfTheDayResponse
import com.android.nasaapod.utils.ApiJsonUtils
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ApiClientRepository : ApiRepositoryInterface {
    private val url = "https://api.nasa.gov/planetary/apod?api_key=${BuildConfig.API_KEY}"

    override suspend fun getPicOfTheDayData(date: String): Result<PicOfTheDayResponse> {
        return makeNetworkRequest(URL("$url&date=$date"))
    }

    override suspend fun getPicOfTheDay(url: String): Bitmap? {
        return try {
            val inputStream = URL(url).openStream()

            if (inputStream != null) {
                BitmapFactory.decodeStream(inputStream)
            } else {
                return null
            }
        } catch (e: IOException) {
            return null
        } catch (e: MalformedURLException) {
            return null
        } catch (e: Exception) {
            return null
        }
    }

    private fun makeNetworkRequest(url: URL): Result<PicOfTheDayResponse> {
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.setRequestProperty("Accept", "application/json")
        urlConnection.doInput = true
        return try {
            val inputStream = urlConnection.inputStream

            if (urlConnection.responseCode != HttpURLConnection.HTTP_OK) {
                return Result.Error(errorMessage())
            }

            if (inputStream != null) {
                Result.Success(ApiJsonUtils.getModelResponse(inputStream))
            } else {
                return Result.Error(errorMessage())
            }

        } catch (e: Exception) {
            return Result.Error(errorMessage())
        } finally {
            urlConnection.disconnect()
        }
    }

    private fun errorMessage(): Exception = Exception("Cannot open HttpURLConnection")
}
