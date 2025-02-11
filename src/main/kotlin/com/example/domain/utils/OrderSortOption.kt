package com.example.domain.utils

enum class OrderSortOption(override val value: String) : SortOption {
    DATE_ASC("date_asc"),
    DATE_DESC("date_desc"),
    STATUS("status")
}