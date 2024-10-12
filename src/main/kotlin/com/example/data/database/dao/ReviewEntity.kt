package com.example.data.database.dao

import com.example.data.database.table.ProductTable
import com.example.data.database.table.ReviewTable
import com.example.data.database.table.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ReviewEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReviewEntity>(ReviewTable)

    val productId by ProductEntity referencedOn ProductTable.id
    val userId by UserEntity referencedOn UserTable.id
    val rating by ReviewTable.rating
    val comment by ReviewTable.comment
    val reviewDate by ReviewTable.reviewDate
}