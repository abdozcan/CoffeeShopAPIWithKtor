package com.example.domain.model

import com.example.data.utils.CartStatus
import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Cart(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    val status: CartStatus,
    @SerialName("created_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @SerialName("expires_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val expiresAt: LocalDateTime?,
    val items: List<CartItem>
)
