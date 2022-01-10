package com.android.nasaapod

import android.graphics.Bitmap

interface ApodInteractor {

    interface View {

        fun setPicOfTheDay(bitmap: Bitmap)

        fun setTitle(title: String)

        fun setDescription(description: String)

        fun showProgressDialog()

        fun stopProgressDialog()

        fun showNetworkErrorDialog()

        fun showErrorDialog()
    }

    interface Presenter {

        fun start()

        suspend fun getPicOfTheDay()

        fun isNetworkConnectionAvailable() : Boolean

        fun loadOfflineData()
    }
}