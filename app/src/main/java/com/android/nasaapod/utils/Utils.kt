package com.android.nasaapod.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun checkIsNetworkConnectAvailable(connectivityManager: ConnectivityManager): Boolean {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return isConnectionOverWifi(networkCapabilities) ?: false || checkConnectionOverCellular(
            networkCapabilities
        ) ?: false
    }

    private fun isConnectionOverWifi(networkCapabilities: NetworkCapabilities?): Boolean? {
        return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    private fun checkConnectionOverCellular(networkCapabilities: NetworkCapabilities?): Boolean? {
        return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    fun getCurrentDate(): String {
        return dateTimeFormat()
    }

    private fun dateTimeFormat(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date()).toString()
    }

}