package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultProductInfo(
    val id: Int,
    val name: String,
    val category: String,
    val imageUrl: String,
)