package com.example.taptravel.fragmentDrawer

data class Place(
    val id: Int?=0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val rating: Double,
    val description: String,
    val category: String,
    val location: String,
    var isInWishlist: Boolean = false

)

