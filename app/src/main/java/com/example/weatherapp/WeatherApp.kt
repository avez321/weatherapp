package com.example.weatherapp

import androidx.multidex.MultiDexApplication
import com.example.weatherapp.di.component.DaggerAppComponent


class WeatherApp : MultiDexApplication() {

    lateinit var appComponent: DaggerAppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build() as DaggerAppComponent

    }

}