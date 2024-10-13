package com.example.domain.model

import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Payment(
    val id: Int,
    @SerialName("order_id")
    val orderId: Int,
    @SerialName("payment_date")
    @Serializable(with = LocalDateTimeSerializer::class)
    val paymentDate: LocalDateTime,
    val amount: Int,
    val method: String
)
