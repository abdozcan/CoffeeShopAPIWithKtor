package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteItem(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("product_id")
    val productId: Int
)