package com.example.data.database.dao

import com.example.data.database.table.ReviewTable
import com.example.domain.model.Review
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ReviewEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReviewEntity>(ReviewTable)

    var productId by ReviewTable.productId
    var userId by ReviewTable.userId
    var rating by ReviewTable.rating
    var comment by ReviewTable.comment
    var reviewDate by ReviewTable.reviewDate

    val user by UserEntity referencedOn ReviewTable

    fun toReview() = Review(
        id = id.value,
        productId = productId.value,
        userName = user.name,
        rating = rating,
        comment = comment,
        reviewDate = reviewDate,
    )
}