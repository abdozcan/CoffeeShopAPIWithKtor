package com.example.domain.repository

import com.example.domain.model.Review

interface ReviewRepository {
    suspend fun findAllByProductId(productId: Int): Result<List<Review>>
    suspend fun findAllByUserId(userId: Int): Result<List<Review>>
    suspend fun add(review: Review): Result<Review>
    suspend fun delete(id: Int): Result<Unit>
}