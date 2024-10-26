package com.example.domain.repository

import com.example.domain.model.Review

interface ReviewRepository {
    suspend fun findAllByProductId(productId: Int, limit: Int, page: Long): Result<List<Review>>
    suspend fun findAllByUserId(userId: Int, limit: Int, page: Long): Result<List<Review>>
    suspend fun add(review: Review): Result<Unit>
    suspend fun edit(review: Review): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}