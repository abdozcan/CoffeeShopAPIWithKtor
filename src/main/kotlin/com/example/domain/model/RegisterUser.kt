package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUser(
    val name: String,
    val password: String,
    val email: String,
    val phone: String
)