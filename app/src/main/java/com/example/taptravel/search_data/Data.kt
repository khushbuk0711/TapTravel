package com.example.taptravel.search_data

import com.example.taptravel.fragmentDrawer.Wishlist

data class Data(
    val is_top_result: Boolean,
    val result_object: ResultObject,
    val result_type: String,
    val review_snippet: ReviewSnippet,
    val scope: String,
    val search_explanations: SearchExplanations,
    var isInWishlist: Boolean =false
)