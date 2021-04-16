package com.example.weatherapp.model


import com.example.weatherapp.network.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object FactsClient {
    val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun makeWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build().create(WeatherApi::class.java)
    }

    fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }
}