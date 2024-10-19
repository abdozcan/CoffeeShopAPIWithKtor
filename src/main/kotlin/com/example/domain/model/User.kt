package com.example.domain.model

import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(
    val id: Int,
    val name: String,
    val password: String,
    val email: String,
    val phone: String,
    @SerialName("default_address")
    val defaultAddress: String?,
    @SerialName("created_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
)
