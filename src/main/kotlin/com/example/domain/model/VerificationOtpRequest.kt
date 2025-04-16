package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VerificationOtpRequest(
    val email: String,
    val otp: String
)