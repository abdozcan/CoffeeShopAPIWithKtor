package com.example.domain.repository

import com.example.domain.model.EditReviewRequest
import com.example.domain.model.Review
import com.example.domain.model.ReviewRequest
import com.example.domain.model.ReviewWithProductInfos
import com.example.domain.utils.ReviewSortOption

interface ReviewRepository {
    suspend fun findAllByProductId(
        productId: Int,
        limit: Int,
        offset: Long,
        sort: ReviewSortOption
    ): Result<List<Review>>

    suspend fun findAllByOrderId(
        orderId: Int,
        userId: Int
    ): Result<List<Review>>

    suspend fun findAllByUserId(
        userId: Int,
        limit: Int,
        offset: Long,
        sort: ReviewSortOption
    ): Result<List<ReviewWithProductInfos>>
    suspend fun add(review: ReviewRequest, userId: Int): Result<Review>
    suspend fun edit(review: EditReviewRequest): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}