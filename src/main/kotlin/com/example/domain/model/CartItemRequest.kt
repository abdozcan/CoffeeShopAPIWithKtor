package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItemRequest(
    @SerialName("product_id")
    val productId: Int,
    val quantity: Int = 1
)