package com.example.data.database.dao

import com.example.data.database.table.OrderItemTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderItemEntity>(OrderItemTable)

    val orderId by OrderItemTable.orderId
    val productId by OrderItemTable.productId
    val quantity by OrderItemTable.quantity
    val price by OrderItemTable.price
}