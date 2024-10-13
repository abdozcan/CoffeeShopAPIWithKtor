package com.example.domain.repository

import com.example.domain.model.Order

interface OrderRepository {
    suspend fun findAllByUserId(userId: Int): Result<List<Order>>
    suspend fun findById(id: Int): Result<Order>
    suspend fun add(userId: Int, productId: Int): Result<Order>
    suspend fun edit(order: Order): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}