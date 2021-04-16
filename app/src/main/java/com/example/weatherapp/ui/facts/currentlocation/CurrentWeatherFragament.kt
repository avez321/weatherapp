package com.example.weatherapp.ui.facts.currentlocation

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherapp.Constants
import com.example.weatherapp.WeatherApp
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCurrentLocationFragamentBinding
import java.util.*
import javax.inject.Inject


class CurrentWeatherFragament : Fragment() {

    lateinit var fragmentCurrentLocationFragamentBinding: FragmentCurrentLocationFragamentBinding

    @Inject
    lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wheatherApp = requireActivity().application as WeatherApp
        wheatherApp.appComponent.inject(this)

    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentCurrentLocationFragamentBinding = FragmentCurrentLocationFragamentBinding.inflate(layoutInflater)

        fragmentCurrentLocationFragamentBinding.viewmodel = currentWeatherViewModel

        fragmentCurrentLocationFragamentBinding.btnShowPicker.setOnClickListener {
            showDatePicker()
        }


        currentWeatherViewModel.getRequestPermissionMutableLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            requestPermissions(
                    Constants.PERMISION_ARRAY,
                    Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
        })


        return fragmentCurrentLocationFragamentBinding.root
    }

    private fun showDatePicker() {
        val newCalendar = Calendar.getInstance();
        val datePicker = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                currentWeatherViewModel.onDateSelected(year, monthOfYear, dayOfMonth)
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))

        datePicker.show()

    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentWeatherViewModel.checkPermision()
                } else {
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.location_permission_not_granted),
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}