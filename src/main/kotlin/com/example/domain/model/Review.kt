package com.example.domain.model

import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Review(
    val id: Int,
    @SerialName("product_id")
    val productId: Int,
    @SerialName("user_name")
    val userName: String,
    val rating: Int,
    val comment: String,
    @SerialName("review_date")
    @Serializable(LocalDateTimeSerializer::class)
    val reviewDate: LocalDateTime
)
