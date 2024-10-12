package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object OrderItemEntity : IntIdTable("order_items") {
    val orderId = integer("order_id").references(OrderTable.id)
    val productId = integer("product_id").references(ProductTable.id)
    val quantity = integer("quantity")
    val price = float("price")
}
