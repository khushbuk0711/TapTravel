package com.example.taptravel.search

data class Ancestor(
    val abbrv: Any,
    val location_id: String,
    val name: String,
    val subcategory: List<SubcategoryX>
)