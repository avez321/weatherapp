package com.example.weatherapp.repository



import com.example.weatherapp.Constants
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.ResultWrapper
import com.example.weatherapp.network.safeApiCall
import com.example.weatherapp.network.WeatherApi


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RepositoryImp(private val weatherApi: WeatherApi, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    RepositoryInterface {
    override suspend fun getCurrentWeatherData(city: String): ResultWrapper<WeatherResponse> {

        return safeApiCall(dispatcher) { weatherApi.getCurrentWeather(Constants.apiKey,city) }
    }
}


