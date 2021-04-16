package com.example.weatherapp.di.component


import android.app.Application
import com.example.weatherapp.di.module.*
import com.example.weatherapp.ui.currentlocation.CurrentWeatherFragament
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

    fun inject(currentWeatherFragment: CurrentWeatherFragament)

    @Component.Builder
     interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}

