package com.example.data.database.table

import com.example.data.utils.OrderStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.CurrentDateTime

object OrderTable : IntIdTable("orders") {
    val userId = reference("user_id", foreign = UserTable)
    val orderDate = datetime("order_date")
    val status = enumerationByName("status", 50, OrderStatus::class)
    val totalAmount = double("total_amount")
    val shippingAddress = text("shipping_address")
    val paymentMethod = varchar("payment_method", 100)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}
