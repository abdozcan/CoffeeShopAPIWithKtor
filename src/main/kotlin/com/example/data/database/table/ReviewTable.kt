package com.example.data.database.table

import com.example.data.utils.Constant.COMMENT_LENGTH
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object ReviewTable : IntIdTable("reviews") {
    val orderId = reference("order_id", foreign = OrderTable, onDelete = ReferenceOption.CASCADE)
    val productId = reference("product_id", foreign = ProductTable, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val rating = integer("rating")
    val comment = varchar("comment", COMMENT_LENGTH)
    val reviewDate = datetime("review_date")
}