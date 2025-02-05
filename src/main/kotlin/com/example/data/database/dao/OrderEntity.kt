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
    var status by OrderTable.status
    var totalAmount by OrderTable.totalAmount
    var shippingAddress by OrderTable.shippingAddress
    var paymentMethod by OrderTable.paymentMethod
    var promoCodeId by OrderTable.promoCodeId
    var createdAt by OrderTable.createdAt
    var updatedAt by OrderTable.updatedAt

    val orderItems by OrderItemEntity referrersOn OrderItemTable.orderId

    fun toOrder() = Order(
        id = id.value,
        orderDate = orderDate,
        status = status,
        totalAmount = totalAmount,
        shippingAddress = shippingAddress,
        paymentMethod = paymentMethod,
        createdAt = createdAt,
        updatedAt = updatedAt,
        orderItems = orderItems.map { it.toOrderItem() }
    )
}
