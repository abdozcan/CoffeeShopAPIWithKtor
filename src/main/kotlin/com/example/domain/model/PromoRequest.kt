package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromoRequest(
    val code: String,
    @SerialName("total_price")
    val totalPrice: Double
)