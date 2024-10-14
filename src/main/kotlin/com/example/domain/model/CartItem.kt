package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Int,
    @SerialName("cart_id")
    val cartId: Int,
    @SerialName("product_id")
    val productId: Int,
    val quantity: Int,
    val price: Double,
    @SerialName("discount_price")
    val discountPrice: Double? = null,
    @SerialName("discount_percentage")
    val discountPercentage: Double? = null
)
