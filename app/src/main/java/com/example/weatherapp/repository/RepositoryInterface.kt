package com.example.weatherapp.repository


import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.ResultWrapper


interface RepositoryInterface {
   suspend fun getCurrentWeatherData(city: String): ResultWrapper<WeatherResponse>
}