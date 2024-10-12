package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val description: String
)