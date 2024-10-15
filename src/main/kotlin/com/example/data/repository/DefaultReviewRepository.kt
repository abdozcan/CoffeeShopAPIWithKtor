package com.example.data.repository

import com.example.data.database.dao.ReviewEntity
import com.example.data.database.table.ProductTable
import com.example.data.database.table.ReviewTable
import com.example.data.database.table.UserTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Review
import com.example.domain.repository.ReviewRepository
import org.jetbrains.exposed.dao.id.EntityID

class DefaultReviewRepository : ReviewRepository {
    override suspend fun findAllByProductId(productId: Int): Result<List<Review>> = runCatching {
        withTransactionContext {
            ReviewEntity.find {
                ReviewTable.productId eq productId
            }.map {
                it.toReview()
            }
        }
    }

    override suspend fun findAllByUserId(userId: Int): Result<List<Review>> = runCatching {
        withTransactionContext {
            ReviewEntity.find {
                ReviewTable.userId eq userId
            }.map {
                it.toReview()
            }
        }
    }

    override suspend fun add(review: Review): Result<Review> = runCatching {
        withTransactionContext {
            ReviewEntity.new {
                this.productId = EntityID(review.productId, ProductTable)
                this.userId = EntityID(review.userId, UserTable)
                this.rating = review.rating
                this.comment = review.comment
                this.reviewDate = review.reviewDate
            }.toReview()
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            ReviewEntity.findById(id).doOrThrowIfNull { it.delete() }
        }
    }
}