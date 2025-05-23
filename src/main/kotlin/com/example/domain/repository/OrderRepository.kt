package com.example.domain.repository

import com.example.domain.model.Order
import com.example.domain.utils.OrderSortOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

interface OrderRepository {
    suspend fun findAllByUserId(
        userId: Int,
        limit: Int,
        offset: Long,
        sortOption: OrderSortOption
    ): Result<List<Order>>
    suspend fun findById(id: Int): Result<Order>
    suspend fun findOrderItemProduct(orderId: Int): Result<List<ProductOfOrderItem>>
    suspend fun cancel(orderId: Int): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
    suspend fun add(
        userId: Int,
        shippingAddress: String,
        paymentMethod: String,
        orderDate: LocalDateTime,
        totalAmount: Double,
        promoCodeId: Int?,
        orderedProducts: List<RequestOrderedProduct>
    ): Result<Order>
}

@Serializable
data class RequestOrderedProduct(
    val id: Int,
    val quantity: Int,
    val price: Double,
)

@Serializable
data class ProductOfOrderItem(
    @SerialName("product_id")
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    @SerialName("image_url")
    val imageUrl: String,
    val category: String
)