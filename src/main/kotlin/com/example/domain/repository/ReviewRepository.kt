package com.example.domain.repository

import com.example.domain.model.Review

interface ReviewRepository {
    suspend fun findAllByProductId(productId: Int): Result<List<Review>>
    suspend fun findAllByUserId(userId: Int): Result<List<Review>>
    suspend fun add(userId: Int, productId: Int): Result<Review>
    suspend fun edit(review: Review): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}