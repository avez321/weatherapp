package com.example.weatherapp.di.module

import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.repository.RepositoryImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideRepositoryImp(weatherApi: WeatherApi): RepositoryImp = RepositoryImp(weatherApi)
}