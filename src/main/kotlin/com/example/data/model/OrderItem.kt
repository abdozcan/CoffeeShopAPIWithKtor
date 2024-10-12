package com.example.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val id: Int,
    @SerialName("order_id")
    val orderId: Int,
    @SerialName("product_id")
    val productId: Int,
    val quantity: Int,
    val price: Float
)
