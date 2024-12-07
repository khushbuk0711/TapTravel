package com.example.taptravel.fragmentDrawer

import com.example.taptravel.SearchApi.PlaceDetails

data class Wishlist(
val items: List<PlaceDetails> = emptyList()
)
