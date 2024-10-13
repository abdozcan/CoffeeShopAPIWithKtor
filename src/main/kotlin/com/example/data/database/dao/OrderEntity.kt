package com.example.data.database.dao

import com.example.data.database.table.OrderItemTable
import com.example.data.database.table.OrderTable
import com.example.domain.model.Order
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(OrderTable)

    var userId by OrderTable.userId
    var orderDate by OrderTable.orderDate
    var amount by OrderTable.amount
    var status by OrderTable.status

    val orderItems by OrderItemEntity referrersOn OrderItemTable.orderId
   /* val products = orderItems.mapNotNull {
        ProductEntity.findById(it.productId)
    }*/

    fun toOrder() = Order(
        id = id.value,
        userId = userId.value,
        orderDate = orderDate,
        amount = amount,
        status = status,
        orderItems = orderItems.map { it.toOrderItem() }
    )
}