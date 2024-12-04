package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultProductInfo(
    val id: Int,
    val name: String,
    val category: String,
    @SerialName("image_url")
    val imageUrl: String
)