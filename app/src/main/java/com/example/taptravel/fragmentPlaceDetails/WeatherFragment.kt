package com.example.taptravel.fragmentDrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taptravel.databinding.FragmentWeatherBinding
import com.example.taptravel.repository.WeatherRepository
import com.example.taptravel.weather.Astronomy
import com.example.taptravel.weather.realtimeWeather
import com.example.taptravel.weatherApi.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherRepository: WeatherRepository

    // Latitude and longitude arguments
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve latitude and longitude from arguments
        arguments?.let {
            latitude = it.getDouble("latitude", 0.0)
            longitude = it.getDouble("longitude", 0.0)
        }

        weatherRepository = WeatherRepository(RetrofitInstance.apiInterface)

        // Fetch current weather and astronomy data and update UI
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        // Using coroutines to handle async network call
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val currentWeatherResponse = weatherRepository.getCurrentWeather("$latitude,$longitude")
                val astronomyResponse = weatherRepository.getAstronomy("$latitude,$longitude")

                withContext(Dispatchers.Main) {
                    // Update UI with the fetched data
                    if (currentWeatherResponse.isSuccessful && astronomyResponse.isSuccessful) {
                        updateUI(currentWeatherResponse.body(), astronomyResponse.body())
                    } else {
                        // Handle error response
                        showError("Failed to fetch weather or astronomy data")
                    }
                }
            } catch (e: Exception) {
                // Handle network or API call exception
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private fun updateUI(currentWeather: realtimeWeather?, astronomy: Astronomy?) {
        currentWeather?.let {
            binding.location.text = it.location.name
            binding.temperature.text = "${it.current.temp_c}°C"
            binding.weatherCondition.text = it.current.condition.text
            binding.humidity.text = "Humidity: ${it.current.humidity}%"
            binding.precipitation.text = "Precipitation: ${it.current.precip_mm} mm"
            binding.windSpeed.text = "Wind Speed: ${it.current.wind_kph} kph"
        }

        astronomy?.let {
            binding.sunrise.text = "Sunrise: ${it.astronomy.astro.sunrise}"
            binding.sunset.text = "Sunset: ${it.astronomy.astro.sunset}"
        }
    }

    private fun showError(message: String) {
        view?.let { view ->
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

