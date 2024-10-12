package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ReviewTable : IntIdTable("reviews") {
    val productId = integer("product_id").references(ProductTable.id)
    val userId = integer("user_id").references(UserTable.id)
    val rating = integer("rating")
    val comment = text("comment")
    val reviewDate = datetime("review_date")
}