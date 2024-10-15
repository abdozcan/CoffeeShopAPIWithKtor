package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("product_id")
    val product: Product,
    val quantity: Int,
    val price: Double,
    @SerialName("discount_price")
    val discountPrice: Double? = null,
    @SerialName("discount_percentage")
    val discountPercentage: Double? = null
)
