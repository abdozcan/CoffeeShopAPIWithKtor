package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object OrderItemTable : IntIdTable("order_items") {
    val orderId = reference(
        "order_id",
        foreign = OrderTable,
        onDelete = ReferenceOption.NO_ACTION,
        onUpdate = ReferenceOption.NO_ACTION
    )
    val productId = reference(
        "product_id",
        foreign = ProductTable,
        onDelete = ReferenceOption.NO_ACTION,
        onUpdate = ReferenceOption.NO_ACTION
    )
    val quantity = integer("quantity")
    val price = double("price")
}
