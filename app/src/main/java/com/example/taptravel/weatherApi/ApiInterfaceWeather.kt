package com.example.taptravel.weatherApi

import com.example.taptravel.weather.Astronomy
import com.example.taptravel.weather.realtimeWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterfaceWeather {

    @Headers(
        "X-RapidAPI-Key: d5438307bemsh340e95ab2226c2ap1d7060jsn149203f0869c",
        "X-RapidAPI-Host: weatherapi-com.p.rapidapi.com"
    )
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String
    ): Response<realtimeWeather>

    @Headers(
        "X-RapidAPI-Key: d5438307bemsh340e95ab2226c2ap1d7060jsn149203f0869c",
        "X-RapidAPI-Host: weatherapi-com.p.rapidapi.com"
    )
    @GET("astronomy.json")
    suspend fun getAstronomy(
        @Query("q") location: String
    ): Response<Astronomy>
}
