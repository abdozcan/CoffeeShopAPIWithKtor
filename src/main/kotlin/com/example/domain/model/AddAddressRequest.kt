package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AddAddressRequest(
    val name: String,
    val address: String
)