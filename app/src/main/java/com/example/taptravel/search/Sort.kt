package com.example.taptravel.search

data class Sort(
    val filter_key: String,
    val label: String,
    val locale_independent_label: String,
    val selected: Boolean
)