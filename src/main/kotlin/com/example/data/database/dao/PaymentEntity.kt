package com.example.data.database.dao

import com.example.data.database.table.OrderTable
import com.example.data.database.table.PaymentTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PaymentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PaymentEntity>(PaymentTable)

    val orderId by OrderEntity referencedOn OrderTable.id
    val paymentDate by PaymentTable.paymentDate
    val amount by PaymentTable.amount
    val method by PaymentTable.method
}