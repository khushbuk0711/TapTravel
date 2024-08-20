package com.example.taptravel.search_data

data class Ancestor(
    val abbrv: Any,
    val location_id: String,
    val name: String,
    val subcategory: List<SubcategoryX>
)