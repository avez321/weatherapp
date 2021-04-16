package com.example.weatherapp.ui.currentlocation


import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.ResultWrapper
import com.example.weatherapp.repository.RepositoryImp
import com.example.weatherapp.util.PermissionUtils
import com.example.weatherapp.util.formatDateForDate
import com.example.weatherapp.util.formatDateForDay
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


class CurrentWeatherViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repositoryImp: RepositoryImp

    private val calender: Calendar = Calendar.getInstance()

    private val cityObservableField: ObservableField<String> = ObservableField()

    private val dayObservableField: ObservableField<String> = ObservableField()

    private val dateObservableField: ObservableField<String> = ObservableField()

    private val temperatureObservableField: ObservableField<String> = ObservableField()

    private val temperatureDescObservableField: ObservableField<String> = ObservableField()

    private val maxTempObservableField: ObservableField<String> = ObservableField()

    private val minTempeDescObservableField: ObservableField<String> = ObservableField()

    private val progressBarVisibility: ObservableField<Int> = ObservableField(View.GONE)

    private val requestPermissionMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun onDateSelected(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if (isPrimeNumber(dayOfMonth)) {
            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, monthOfYear)
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            checkPermision()
        } else {
            Toast.makeText(getApplication(), "Not a prime number", Toast.LENGTH_SHORT).show()
            clearValues()
        }
    }

    private fun clearValues() {
        cityObservableField.set("")
        dayObservableField.set("")
        dateObservableField.set("")
        temperatureObservableField.set("")
        temperatureDescObservableField.set("")
        maxTempObservableField.set("")
        minTempeDescObservableField.set("")
    }


    fun checkPermision() {
        when {
            PermissionUtils.isAccessFineLocationGranted(context) -> {
                when {
                    PermissionUtils.isLocationEnabled(context) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(context)
                    }
                }
            }
            else -> {
                requestPermissionMutableLiveData.value = true
            }
        }
    }


    private fun callWeatherApi(city: String) {
        viewModelScope.launch {
            val resultWarraper = repositoryImp.getCurrentWeatherData(city)
            withContext(Dispatchers.Main) {
                when (resultWarraper) {
                    is ResultWrapper.NetworkError -> showNetworkError()
                    is ResultWrapper.GenericError -> showGenericError(resultWarraper)
                    is ResultWrapper.Success ->
                        showSuccess(resultWarraper.value)
                }
            }
        }
    }

    private fun showSuccess(value: WeatherResponse) {
        progressBarVisibility.set(View.GONE)
        cityObservableField.set(value.name)

        dayObservableField.set(calender.formatDateForDay())

        dateObservableField.set(calender.formatDateForDate())

        temperatureObservableField.set(String.format(context.getString(R.string.temprature), value.main?.temp.toString()))

        temperatureDescObservableField.set(value.weather?.get(0)?.description)

        maxTempObservableField.set(String.format(context.getString(R.string.max_temprature), value.main?.temp_max.toString()))

        minTempeDescObservableField.set(String.format(context.getString(R.string.min_temprature), value.main?.temp_min.toString()))

    }

    private fun showGenericError(resultWarraper: ResultWrapper.GenericError) {

    }

    private fun showNetworkError() {

    }


    fun getCityObservableField() = cityObservableField

    fun getDayObservableField() = dayObservableField

    fun getDateObservableField() = dateObservableField

    fun getTemperatureObservableField() = temperatureObservableField

    fun getTemperatureDescObservableField() = temperatureDescObservableField

    fun getMaxTempObservableField() = maxTempObservableField

    fun getMinTempeDescObservableField() = minTempeDescObservableField

    fun getRequestPermissionMutableLiveData() = requestPermissionMutableLiveData

    fun getProgressBarVisibility() = progressBarVisibility


    fun isPrimeNumber(number: Int): Boolean {

        for (i in 2..number / 2) {
            if (number % i == 0) {
                return false
            }
        }
        return true
    }


    private fun setUpLocationListener() {
        clearValues()
        progressBarVisibility.set(View.VISIBLE)
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest.create().apply {
            setInterval(2000).setFastestInterval(2000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        }


        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        if (locationResult.locations.size > 0) {
                            val geocoder = Geocoder(getApplication(), Locale.getDefault())
                            val location = locationResult.locations[0]
                            val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            val cityName: String = addresses[0].locality
                            callWeatherApi(cityName)
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }

                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                Looper.myLooper()
        )
    }


}

