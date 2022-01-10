package com.android.nasaapod.utils

import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
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
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date()).toString()
    }

    fun saveBitmapToStorage(bitmap: Bitmap, absolutePath: String): String {
        try {
            var myDir = File(absolutePath + "/apod")

            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            val name = getCurrentDate() + ".jpg"
            myDir = File(myDir, name)
            val out = FileOutputStream(myDir)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)

            out.flush()
            out.close()

            return "$myDir"

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}