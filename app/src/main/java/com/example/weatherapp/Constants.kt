package com.example.weatherapp

import android.Manifest

object Constants {
    val PERMISION_ARRAY: Array<String> =  arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    const val LOCATION_PERMISSION_REQUEST_CODE =1001
    const val PREF_NAME: String  = "pref_name"
    const val apiKey = "8b7ca59040fa19bf3187a21f0049993a"
    const val country = "in"
    const val FACT_TITLE: String  = "fact_title"



}