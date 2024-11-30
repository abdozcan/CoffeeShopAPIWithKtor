package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CartDeleteRequest(
    val ids: List<Int>
)