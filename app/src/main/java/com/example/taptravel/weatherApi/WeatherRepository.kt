package com.example.taptravel.repository

import com.example.taptravel.weatherApi.ApiInterfaceWeather
import com.example.taptravel.weather.Astronomy
import com.example.taptravel.weather.realtimeWeather
import retrofit2.Response

class WeatherRepository(private val weatherApi: ApiInterfaceWeather) {

    suspend fun getCurrentWeather(location: String): Response<realtimeWeather> {
        return weatherApi.getCurrentWeather(location)
    }

    suspend fun getAstronomy(location: String): Response<Astronomy> {
        return weatherApi.getAstronomy(location)
    }
}
