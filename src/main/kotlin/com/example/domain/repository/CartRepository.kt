package com.example.domain.repository

import com.example.domain.model.CartItem

interface CartRepository {
    suspend fun findAllByUserId(userId: Int): Result<List<CartItem>>
    suspend fun add(userId: Int, productId: Int, quantity: Int): Result<Unit>
    suspend fun delete(ids: List<Int>): Result<Unit>
}