package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditReviewRequest(
    val id: Int,
    @SerialName("product_id")
    val productId: Int,
    val rating: Int,
    val comment: String
)