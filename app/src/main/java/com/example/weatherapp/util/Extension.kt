package com.example.weatherapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


fun Context.showtoast(msg: String, lenght: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, lenght).show()
}


fun Calendar.formatDateForDate(): String {
    val dt = SimpleDateFormat("dd/MM/yyyy")
    return dt.format(time)
}


fun Calendar.formatDateForDay(): String {
    val dt = SimpleDateFormat("EEEE")
    return dt.format(time)
}



 fun Context.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }
    return result
}

