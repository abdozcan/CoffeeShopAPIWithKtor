package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object OrderItemTable : IntIdTable("order_items") {
    val orderId = reference("order_id", foreign = OrderTable, onDelete = ReferenceOption.CASCADE)
    val productId = reference("product_id", foreign = ProductTable)
    val quantity = integer("quantity")
    val price = double("price")
}
