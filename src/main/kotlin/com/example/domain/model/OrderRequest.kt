package com.example.domain.model

import com.example.domain.repository.RequestOrderedProduct
import com.example.domain.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class OrderRequest(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("shipping_address")
    val shippingAddress: String,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("order_date")
    @Serializable(LocalDateTimeSerializer::class)
    val orderDate: LocalDateTime,
    @SerialName("ordered_products")
    val orderedProducts: List<RequestOrderedProduct>
)