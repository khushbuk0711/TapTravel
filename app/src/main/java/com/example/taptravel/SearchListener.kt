package com.example.taptravel

import com.example.taptravel.search.Destination

interface SearchListener {
    fun onResponse(response: Destination?)
    fun onError(msg : String)
}