package com.example.taptravel.weatherApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit by lazy {
        val client= OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        Retrofit.Builder().baseUrl("https://weatherapi-com.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiInterface by lazy{
        retrofit.create(ApiInterfaceWeather::class.java)
    }
}