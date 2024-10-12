package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val id: Int,
    val name: String,
    @SerialName("user_id")
    val userId: Int,
    val address: String
)
