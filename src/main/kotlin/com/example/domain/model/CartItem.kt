package com.example.domain.model

import com.example.data.utils.CartStatus
import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CartItem(
    val id: Int,
    @SerialName("product_id")
    val productId: Int,
    val name: String,
    val weight: Int,
    @SerialName("image_url")
    val imageUrl: String,
    val quantity: Int,
    @SerialName("stock_quantity")
    val stockQuantity: Int,
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