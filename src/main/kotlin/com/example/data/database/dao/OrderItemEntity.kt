package com.example.data.database.dao

import com.example.data.database.table.OrderItemTable
import com.example.data.database.table.OrderTable
import com.example.data.database.table.ProductTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderItemEntity>(OrderItemTable)

    val orderId by OrderEntity referencedOn OrderTable.id
    val productId by ProductEntity referencedOn ProductTable.id
    val quantity by OrderItemTable.quantity
    val price by OrderItemTable.price
}