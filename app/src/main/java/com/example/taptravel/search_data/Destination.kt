package com.example.taptravel.search_data

data class Destination(
    val `data`: List<Data>,
    val metadata: Metadata,
    val paging: Paging,
    val partial_content: Boolean,
    val sort: List<Sort>,
    val tracking: Tracking
)