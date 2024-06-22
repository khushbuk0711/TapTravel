package com.example.taptravel

import com.example.taptravel.search.Destination
import okhttp3.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api_interface {
    @Headers(
        "X-RapidAPI-Key: d5438307bemsh340e95ab2226c2ap1d7060jsn149203f0869c",
        "X-RapidAPI-Host: travel-advisor.p.rapidapi.com"
    )
    @GET("locations/search")
    fun searchPlaces(
        @Query("query") query: String,
    ): Response<Destination>

//    @GET("locations/auto-complete")
//    fun autoComplete(
//        @Query("query") query: String,
//        @Query("lang") lang: String = "en_US",
//        @Query("units") units: String = "km",
//    ): Response<Destination>
//


}

