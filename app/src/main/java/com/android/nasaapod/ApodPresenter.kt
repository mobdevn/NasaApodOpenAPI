package com.android.nasaapod

import android.graphics.Bitmap
import android.net.ConnectivityManager
import com.android.nasaapod.utils.Utils
import com.android.nasaapod.network.model.PicOfTheDayResponse
import com.android.nasaapod.network.repository.ApiRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApodPresenter(
    apiClientRepository: ApiRepositoryInterface,
    networkConnectivityManager: ConnectivityManager,
    uiView: ApodInteractor.View
) : ApodInteractor.Presenter {

    private val connectivityManager: ConnectivityManager = networkConnectivityManager
    private val apiRepository: ApiRepositoryInterface = apiClientRepository
    private val view: ApodInteractor.View = uiView

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
            TODO("Not yet implemented")
        }
    }

    private fun saveData() {
        TODO("Not yet implemented")
    }

    private fun setupData(data: PicOfTheDayResponse, bitmap: Bitmap) {
        TODO("Not yet implemented")
    }

    override fun loadOfflineData() {
        TODO("Not yet implemented")
    }
}