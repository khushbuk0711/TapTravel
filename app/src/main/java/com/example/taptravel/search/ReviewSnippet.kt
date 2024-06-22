package com.example.taptravel.search

data class ReviewSnippet(
    val review_id: String,
    val snippet: String,
    val spans: List<Span>
)