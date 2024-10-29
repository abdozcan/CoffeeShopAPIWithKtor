package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val region: String,
    val weight: Int,
    @SerialName("flavor_profile")
    val flavorProfile: String,
    @SerialName("grind_option")
    val grindOption: String,
    val roastLevel: Int,
    @SerialName("image_url")
    val imageUrl: String,
    val category: String,
    @SerialName("stock_quantity")
    val stockQuantity: Int,
    @SerialName("popularity_rating")
    val popularityRating: Float,
    @SerialName("discount_price")
    val discountPrice: Double?,
    @SerialName("discount_percentage")
    val discountPercentage: Double?,
    @SerialName("is_bestseller")
    val isBestseller: Boolean,
    @SerialName("is_favorite")
    val isFavorite: Boolean?,
    val reviews: List<Review>
)