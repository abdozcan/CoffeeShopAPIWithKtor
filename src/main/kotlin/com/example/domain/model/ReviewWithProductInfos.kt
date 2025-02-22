package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewWithProductInfos(
    val review: Review,
    val name: String,
    val weight: Int,
    @SerialName("image_url")
    val imageUrl: String,
)