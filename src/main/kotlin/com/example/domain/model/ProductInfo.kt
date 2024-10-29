package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductInfo(
    val id: Int,
    val name: String,
    val price: Double,
    val weight: Int,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("stock_quantity")
    val stockQuantity: Int,
    @SerialName("popularity_rating")
    val popularityRating: Float,
    @SerialName("discount_price")
    val discountPrice: Double?,
    @SerialName("discount_percentage")
    val discountPercentage: Double?
)