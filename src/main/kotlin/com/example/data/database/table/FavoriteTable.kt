package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object FavoriteTable : IntIdTable("favorites") {
    val userId = integer("user_id").references(UserTable.id)
    val productId = integer("product_id").references(ProductTable.id)
}
