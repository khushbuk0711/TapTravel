package com.example.taptravel.SearchApi

data class PlaceDetails(
    val id: String? = null,
    val name: String? = null,
    val img: String? = null,
    val category: String?=null,
    val description: String? = null,
    var isInWishlist: Boolean = false
)
