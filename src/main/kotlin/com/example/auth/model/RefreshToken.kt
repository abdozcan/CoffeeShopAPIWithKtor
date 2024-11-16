package com.example.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    @SerialName("refresh_token")
    val refreshToken: String
)