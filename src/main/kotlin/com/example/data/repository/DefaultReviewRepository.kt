package com.example.data.repository

import com.example.data.database.dao.ProductEntity
import com.example.data.database.dao.ReviewEntity
import com.example.data.database.table.ProductTable
import com.example.data.database.table.ReviewTable
import com.example.data.database.table.UserTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.EditReviewRequest
import com.example.domain.model.Review
import com.example.domain.model.ReviewRequest
import com.example.domain.repository.ReviewRepository
import com.example.domain.utils.ReviewSortOption
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import java.time.LocalDateTime

class DefaultReviewRepository : ReviewRepository {
    override suspend fun findAllByProductId(
        productId: Int,
        limit: Int,
        offset: Long,
        sort: ReviewSortOption
    ): Result<List<Review>> =
        runCatching {
            withTransactionContext {
                ReviewEntity.find {
                    ReviewTable.productId eq productId
                }.limit(limit, offset).sortBy(sort).map {
                    it.toReview()
                }
            }
        }

    override suspend fun findAllByUserId(
        userId: Int,
        limit: Int,
        offset: Long,
        sort: ReviewSortOption
    ): Result<List<Review>> = runCatching {
        withTransactionContext {
            ReviewEntity.find {
                ReviewTable.userId eq userId
            }.limit(limit, offset).sortBy(sort).map {
                it.toReview()
            }
        }
    }

    override suspend fun add(review: ReviewRequest, userId: Int): Result<Review> = runCatching {
        withTransactionContext {
            ProductEntity.findByIdAndUpdate(id = review.productId) {
                val reviewsCount = it.reviews.count()
                it.popularityRating = ((it.popularityRating * reviewsCount) + review.rating) / (reviewsCount + 1)
            }

            ReviewEntity.new {
                this.productId = EntityID(review.productId, ProductTable)
                this.userId = EntityID(userId, UserTable)
                this.rating = review.rating
                this.comment = review.comment
                this.reviewDate = LocalDateTime.now()
            }.toReview()
        }
    }

    override suspend fun edit(review: EditReviewRequest): Result<Unit> = runCatching {
        withTransactionContext {
            ProductEntity.findByIdAndUpdate(id = review.productId) {
                val oldRating = it.reviews.find { it.id.value == review.id }!!.rating
                val reviewsCount = it.reviews.count()
                it.popularityRating = ((it.popularityRating * reviewsCount) - oldRating + review.rating) / reviewsCount
            }

            ReviewEntity.findByIdAndUpdate(review.id) {
                it.rating = review.rating
                it.comment = review.comment
            }
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            ProductEntity.findByIdAndUpdate(id = ReviewEntity.findById(id)!!.productId.value) {
                val reviewsCount = it.reviews.count()
                val oldRating = it.reviews.find { it.id.value == id }!!.rating
                it.popularityRating = (it.popularityRating * reviewsCount - oldRating) / (reviewsCount - 1)
            }

            ReviewEntity.findById(id).doOrThrowIfNull { it.delete() }
        }
    }
}

private fun <T> SizedIterable<T>.sortBy(sort: ReviewSortOption): SizedIterable<T> =
    orderBy(
        when (sort) {
            ReviewSortOption.RATING_ASC -> ReviewTable.rating to SortOrder.ASC
            ReviewSortOption.RATING_DESC -> ReviewTable.rating to SortOrder.DESC
            ReviewSortOption.DATE_DESC -> ReviewTable.reviewDate to SortOrder.DESC
            ReviewSortOption.DATE_ASC -> ReviewTable.reviewDate to SortOrder.ASC
        }
    )