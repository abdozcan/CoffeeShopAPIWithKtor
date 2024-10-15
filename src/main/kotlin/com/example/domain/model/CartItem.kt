package com.example.domain.model

import com.example.data.utils.CartStatus
import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

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
    val discountPercentage: Double? = null,
    @SerialName("expires_at")
    @Serializable(LocalDateTimeSerializer::class)
    val expiresAt: LocalDateTime,
    val status: CartStatus
)
