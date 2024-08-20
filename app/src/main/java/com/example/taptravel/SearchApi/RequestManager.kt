package com.example.taptravel.SearchApi

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RequestManager(var context: Context) {


    fun searchPlaces(placeName : String, listener: SearchListener) {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val response = Retrofit_instancePlace.apiInterface.searchPlaces(placeName)

                if (response.isSuccessful()) {
                    listener.onResponse(response.body())
                }
                else{
                    null
                }

            } catch (e: Exception) {
                Log.d("RequestManager", "Error: ${e.message}")
            }

        }
    }
}