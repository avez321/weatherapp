package com.example.weatherapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.di.factory.ViewModelFactory
import com.example.weatherapp.di.ViewModelKey
import com.example.weatherapp.di.scope.FragmentScoped
import com.example.weatherapp.ui.currentlocation.CurrentWeatherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @FragmentScoped
    @ViewModelKey(CurrentWeatherViewModel::class)
    abstract fun bindCurrentLocationViewModel(viewModel: CurrentWeatherViewModel): ViewModel
}