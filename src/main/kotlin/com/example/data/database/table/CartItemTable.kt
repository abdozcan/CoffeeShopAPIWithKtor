package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object CartItemTable : IntIdTable("car_item") {
    val cartId = reference("cart_id", foreign = CartTable)
    val productId = reference("product_id", foreign = ProductTable)
    val quantity = integer("quantity")
    val price = double("price")
    val discountPrice = double("discount_price").nullable()
    val discountPercentage = double("discount_percentage").nullable()
}