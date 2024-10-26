package com.example.domain.utils

enum class ProductSortOption(override val value: String) : SortOption {
    PRICE_ASC("price_asc"),
    PRICE_DESC("price_desc"),
    POPULARITY("popularity"),
    DATE_DESC("date_desc"),
    DATE_ASC("date_asc")
}