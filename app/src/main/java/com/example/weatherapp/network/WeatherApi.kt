package com.example.weatherapp.network


import com.example.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(@Query("appid") apikey:String,@Query("q") query:String ): WeatherResponse
}