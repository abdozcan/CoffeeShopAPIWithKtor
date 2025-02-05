package com.example.domain.model

import com.example.domain.repository.RequestOrderedProduct
import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class OrderRequest(
    @SerialName("shipping_address")
    val shippingAddress: String,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("order_date")
    @Serializable(LocalDateTimeSerializer::class)
    val orderDate: LocalDateTime,
    @SerialName("total_amount")
    val totalAmount: Double,
    @SerialName("promo_code_id")
    val promoCodeId: Int?,
    @SerialName("ordered_products")
    val orderedProducts: List<RequestOrderedProduct>
)