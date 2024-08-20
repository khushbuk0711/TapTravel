package com.example.taptravel.SearchApi

import com.example.taptravel.search_data.Destination

interface SearchListener {
    fun onResponse(response: Destination?)
    fun onError(msg : String)
}