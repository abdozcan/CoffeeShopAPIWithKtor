package com.example.domain.model

import com.example.data.utils.DiscountType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromoRespond(
    @SerialName("promo_code_id")
    val promoCodeId: Int,
    val discount: Float,
    @SerialName("discount_type")
    val discountType: DiscountType,
    @SerialName("min_total_price")
    val minTotalPrice: Double
)