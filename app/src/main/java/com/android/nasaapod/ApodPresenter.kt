package com.android.nasaapod

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.util.Log
import com.android.nasaapod.utils.Utils
import com.android.nasaapod.network.model.PicOfTheDayResponse
import com.android.nasaapod.network.repository.ApiRepositoryInterface
import com.android.nasaapod.network.repository.Result
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class ApodPresenter(
    apiClientRepository: ApiRepositoryInterface,
    networkConnectivityManager: ConnectivityManager,
    sharedPreferences: SharedPreferences,
    uiView: ApodInteractor.View
) : ApodInteractor.Presenter {

    private val connectivityManager: ConnectivityManager = networkConnectivityManager
    private val apiRepository: ApiRepositoryInterface = apiClientRepository
    private val sharedPref: SharedPreferences = sharedPreferences
    private val view: ApodInteractor.View = uiView

    var picOfTheDayResponse: PicOfTheDayResponse? = null

    override fun start() {
        if (isNetworkConnectionAvailable()) {
            GlobalScope.launch {
                getPicOfTheDay()
            }
        } else {
            view.showNetworkErrorDialog()
            loadOfflineData()
        }
    }

    override fun isNetworkConnectionAvailable(): Boolean {
        return Utils.checkIsNetworkConnectAvailable(connectivityManager)
    }

    override suspend fun getPicOfTheDay() {
        view.showProgressDialog()
        withContext(Dispatchers.IO) {
            val result = apiRepository.getPicOfTheDayData(Utils.getCurrentDate())
            when (result) {
                is Result.Success<PicOfTheDayResponse> -> {
                    try {
                        val data = result.data
                        var bitmap: Bitmap? = null
                        if (data.hdurl != null && data.hdurl.isNotEmpty() && data.media_type.equals("image")) {
                            bitmap = apiRepository.getPicOfTheDay(data.hdurl)
                        } else if (data.url != null && data.url.isNotEmpty() && data.media_type.equals("image")) {
                            bitmap = apiRepository.getPicOfTheDay(data.url)
                        }
                        setupData(data, bitmap)
                    } catch (e: Exception) {
                        showDialog()
                        e.printStackTrace()
                    }
                }
                else -> {
                    showDialog()
                }
            }
        }
    }

    private fun setupData(data: PicOfTheDayResponse, bitmap: Bitmap?) {
        GlobalScope.launch(Dispatchers.Main) {
            view.setDescription(data.explanation)
            view.setTitle(data.title)
            view.stopProgressDialog()
            var filePath = ""
            if (bitmap != null) {
                view.setPicOfTheDay(bitmap)
                filePath = Utils.saveBitmapToStorage(bitmap, view.getAbsolutePath() ?: "")
            }
            saveData(data, filePath)
        }
    }

    private fun showDialog() {
        GlobalScope.launch(Dispatchers.Main) {
            view.stopProgressDialog()
            view.showErrorDialog()
            loadOfflineData()
        }
    }

    private fun saveData(data: PicOfTheDayResponse, filePath: String) {
        val jsonObj = Gson().toJson(data)
        with(sharedPref.edit()) {
            putString("jsonobj", jsonObj)
            putString("filepath", filePath)
            commit()
        }
    }

    override fun loadOfflineData() {
        val jsonObjString = sharedPref.getString("jsonobj", "")
        val filePath = sharedPref.getString("filepath", "")
        if (jsonObjString != null && jsonObjString.isNotEmpty()) {
            picOfTheDayResponse = Gson().fromJson(jsonObjString, PicOfTheDayResponse::class.java)
            view.setTitle(picOfTheDayResponse?.title ?: "default_title")
            view.setDescription(picOfTheDayResponse?.explanation ?: "default_explanation")
        }

        if (filePath != null && filePath.isNotEmpty()) {
            val imageFile = File(filePath)
            if (imageFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                view.setPicOfTheDay(myBitmap)
            }
        }
    }
}