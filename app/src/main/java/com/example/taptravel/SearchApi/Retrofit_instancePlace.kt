package com.example.taptravel.SearchApi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object Retrofit_instancePlace {
    val retrofit by lazy {
        val client=OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        Retrofit.Builder().baseUrl("https://travel-advisor.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiInterface by lazy{
        retrofit.create(Api_interfacePlace::class.java)
    }

}