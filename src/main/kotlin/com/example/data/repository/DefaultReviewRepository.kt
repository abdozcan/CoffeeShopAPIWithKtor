package com.example.data.repository

import com.example.data.database.dao.ReviewEntity
import com.example.data.database.table.ProductTable
import com.example.data.database.table.ReviewTable
import com.example.data.database.table.UserTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Review
import com.example.domain.repository.ReviewRepository
import com.example.domain.utils.ReviewSortOption
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder

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

    override suspend fun add(review: Review): Result<Unit> = runCatching {
        withTransactionContext {
            ReviewEntity.new {
                this.productId = EntityID(review.productId, ProductTable)
                this.userId = EntityID(review.userId!!, UserTable)
                this.rating = review.rating
                this.comment = review.comment
                this.reviewDate = review.reviewDate
            }.toReview()
        }
    }

    override suspend fun edit(review: Review): Result<Unit> = runCatching {
        withTransactionContext {
            ReviewEntity.findByIdAndUpdate(review.id) {
                it.rating = review.rating
                it.comment = review.comment
                it.reviewDate = review.reviewDate
            }
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
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