package com.example.taptravel.search

data class CategoryCounts(
    val accommodations: Accommodations,
    val airports: String,
    val attractions: Attractions,
    val neighborhoods: String,
    val restaurants: Restaurants
)