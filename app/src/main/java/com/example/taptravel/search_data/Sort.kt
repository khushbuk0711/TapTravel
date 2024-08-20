package com.example.taptravel.search_data

data class Sort(
    val filter_key: String,
    val label: String,
    val locale_independent_label: String,
    val selected: Boolean
)