package com.example.data.database.table

import com.example.data.utils.CartStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object CartItemTable : IntIdTable("cart_item") {
    val userId = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val productId = reference("product_id", foreign = ProductTable, onDelete = ReferenceOption.CASCADE)
    val quantity = integer("quantity")
    val price = double("price")
    val discountPrice = double("discount_price").nullable()
    val discountPercentage = double("discount_percentage").nullable()
    val expiresAt = datetime("expires_at")
    val status = enumeration<CartStatus>("status")
}