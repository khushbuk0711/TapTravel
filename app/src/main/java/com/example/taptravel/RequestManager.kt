package com.example.taptravel

import android.content.Context
import com.example.taptravel.search.Destination


class RequestManager(var context: Context) {


    fun searchPlaces(placeName : String, listener: SearchListener) {

        try {
            val response = Retrofit_instance.apiInterface.searchPlaces(placeName)

            if(response.isSuccessful()){
                listener.onResponse(response.body())
            }
            else{
                null
            }

        } catch (e: Exception) {}

    }
}