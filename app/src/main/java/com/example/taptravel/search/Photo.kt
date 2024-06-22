package com.example.taptravel.search

data class Photo(
    val caption: String,
    val helpful_votes: String,
    val id: String,
    val images: Images,
    val is_blessed: Boolean,
    val published_date: String,
    val uploaded_date: String,
    val user: User
)