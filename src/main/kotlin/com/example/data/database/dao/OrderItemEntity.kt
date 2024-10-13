package com.example.data.database.dao

import com.example.data.database.table.OrderItemTable
import com.example.domain.model.OrderItem
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderItemEntity>(OrderItemTable)

    val orderId by OrderItemTable.orderId
    val productId by OrderItemTable.productId
    val quantity by OrderItemTable.quantity
    val price by OrderItemTable.price

    fun toOrderItem() = OrderItem(
        id = id.value,
        orderId = orderId.value,
        productId = productId.value,
        quantity = quantity,
        price = price
    )
}