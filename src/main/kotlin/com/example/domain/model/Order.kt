package com.example.domain.model

import com.example.data.utils.LocalDateTimeSerializer
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
    val amount: Int,
    val status: String
)
