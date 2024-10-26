package com.example.domain.utils

enum class ReviewSortOption(override val value: String) : SortOption {
    DATE_ASC("date_asc"),
    DATE_DESC("date_desc"),
    RATING_ASC("rating_asc"),
    RATING_DESC("rating_desc")
}