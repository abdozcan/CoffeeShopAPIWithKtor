package com.example.domain.repository

import com.example.domain.model.Order
import java.time.LocalDateTime

interface OrderRepository {
    suspend fun findAllByUserId(userId: Int): Result<List<Order>>
    suspend fun findById(id: Int): Result<Order>
    suspend fun edit(order: Order): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
    suspend fun add(
        userId: Int,
        shippingAddress: String,
        paymentMethod: String,
        orderDate: LocalDateTime,
        orderedProducts: List<OrderedProduct>
    ): Result<Order>
}

data class OrderedProduct(
    val id: Int,
    val quantity: Int,
    val price: Double,
)