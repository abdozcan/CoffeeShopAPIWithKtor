package com.example.data.database.dao

import com.example.data.database.table.ReviewTable
import com.example.data.database.table.UserTable
import com.example.domain.model.Review
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ReviewEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReviewEntity>(ReviewTable)

    val productId by ReviewTable.productId
    val userId by ReviewTable.userId
    val rating by ReviewTable.rating
    val comment by ReviewTable.comment
    val reviewDate by ReviewTable.reviewDate

    val users by UserEntity referrersOn UserTable.id

    fun toReview() = Review(
        id = id.value,
        productId = productId.value,
        userId = userId.value,
        rating = rating,
        comment = comment,
        reviewDate = reviewDate
    )
}