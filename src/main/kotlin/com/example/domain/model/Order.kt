package com.example.domain.model

import com.example.data.utils.OrderStatus
import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Order(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("order_date")
    @Serializable(with = LocalDateTimeSerializer::class)
    val orderDate: LocalDateTime,
    val status: OrderStatus,
    @SerialName("total_amount")
    val totalAmount: Double,
    @SerialName("shipping_address")
    val shippingAddress: String,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("created_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
    @SerialName("order_items")
    val orderItems: List<OrderItem>
)
