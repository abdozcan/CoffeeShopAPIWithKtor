package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object FavoriteTable : IntIdTable("favorites") {
    val userId = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val productId = reference("product_id", foreign = ProductTable, onDelete = ReferenceOption.CASCADE)
}