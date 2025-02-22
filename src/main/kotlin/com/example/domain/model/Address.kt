package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val id: Int,
    val name: String,
    val address: String,
    @SerialName("is_default")
    val isDefault: Boolean
)
