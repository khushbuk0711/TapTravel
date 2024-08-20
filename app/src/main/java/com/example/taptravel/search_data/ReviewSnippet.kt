package com.example.taptravel.search_data

data class ReviewSnippet(
    val review_id: String,
    val snippet: String,
    val spans: List<Span>
)