package com.example.domain.model

import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Promotion(
    val id: Int,
    @SerialName("product_id")
    val productId: Int,
    val discount: Float,
    @SerialName("start_date")
    @Serializable(LocalDateTimeSerializer::class)
    val startDate: LocalDateTime,
    @SerialName("end_date")
    @Serializable(LocalDateTimeSerializer::class)
    val endDate: LocalDateTime,
    val description: String
)
