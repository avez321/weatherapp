package com.example.weatherapp.di.module

import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.model.FactsClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi = FactsClient.makeWeatherApi()
}