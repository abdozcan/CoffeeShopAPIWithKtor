package com.example.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class Credential(
    val email: String,
    val password: String
)