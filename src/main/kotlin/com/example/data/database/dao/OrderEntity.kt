package com.example.data.database.dao

import com.example.data.database.table.OrderItemTable
import com.example.data.database.table.OrderTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(OrderTable)

    val userId by OrderTable.userId
    val orderDate by OrderTable.orderDate
    val amount by OrderTable.amount
    val status by OrderTable.status

    private val orderItems by OrderItemEntity referrersOn OrderItemTable.orderId
   /* val products = orderItems.mapNotNull {
        ProductEntity.findById(it.productId)
    }*/
}