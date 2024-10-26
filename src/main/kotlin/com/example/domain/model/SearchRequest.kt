package com.example.domain.model

import com.example.domain.utils.ProductSortOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    val name: String,
    val category: String?,
    @SerialName("min_price")
    val minPrice: Double = 0.0,
    @SerialName("max_price")
    val maxPrice: Double = Double.MAX_VALUE,
    val sort: ProductSortOption = ProductSortOption.POPULARITY,
    val page: Long = 1,
    val limit: Int = 10
)