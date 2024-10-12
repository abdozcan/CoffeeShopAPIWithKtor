package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: Int,
    @SerialName("product_id")
    val productId: Int,
    @SerialName("user_id")
    val userId: Int,
    val rating: Int,
    val comment: String,
    @SerialName("review_date")
    val reviewDate: String
)
