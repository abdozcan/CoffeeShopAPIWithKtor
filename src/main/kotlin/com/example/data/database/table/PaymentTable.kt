package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object PaymentTable : IntIdTable("payments") {
    val orderId = integer("order_id").references(OrderTable.id)
    val paymentDate = datetime("payment_date")
    val amount = integer("amount")
    val method = text("method")
}