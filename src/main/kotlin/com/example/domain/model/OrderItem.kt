package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val id: Int,
    @SerialName("product_id")
    val productId: Int,
    val name: String,
    val quantity: Int,
    val weight: Int,
    val price: Double,
    @SerialName(value = "discount_price")
    val discountPrice: Double?,
    @SerialName("image_url")
    val imageUrl: String
)
