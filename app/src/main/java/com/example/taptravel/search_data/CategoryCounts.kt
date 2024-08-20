package com.example.taptravel.search_data

data class CategoryCounts(
    val accommodations: Accommodations,
    val airports: String,
    val attractions: Attractions,
    val neighborhoods: String,
    val restaurants: Restaurants
)