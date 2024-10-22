package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object PaymentTable : IntIdTable("payments") {
    val orderId = reference("order_id", foreign = OrderTable, onDelete = ReferenceOption.NO_ACTION)
    val paymentDate = datetime("payment_date")
    val amount = integer("amount")
    val method = text("method")
}