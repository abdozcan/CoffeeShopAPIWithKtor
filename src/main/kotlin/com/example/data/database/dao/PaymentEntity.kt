package com.example.data.database.dao

import com.example.data.database.table.PaymentTable
import com.example.domain.model.Payment
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PaymentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PaymentEntity>(PaymentTable)

    var orderId by PaymentTable.orderId
    var paymentDate by PaymentTable.paymentDate
    var amount by PaymentTable.amount
    var method by PaymentTable.method
    var status by PaymentTable.status
    var transactionId by PaymentTable.transactionId
    var createdAt by PaymentTable.createdAt
    var updatedAt by PaymentTable.updatedAt

    fun toPayment() = Payment(
        id = id.value,
        orderId = orderId.value,
        paymentDate = paymentDate,
        amount = amount,
        method = method,
        status = status
    )
}