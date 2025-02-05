package com.example.data.database.table

import com.example.domain.model.PaymentStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object PaymentTable : IntIdTable("payments") {
    val orderId = reference("order_id", foreign = OrderTable, onDelete = ReferenceOption.NO_ACTION)
    val paymentDate = datetime("payment_date")
    val amount = double("amount")
    val method = text("method")
    val status = enumeration<PaymentStatus>("status")
    val transactionId = text("transaction_id")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}