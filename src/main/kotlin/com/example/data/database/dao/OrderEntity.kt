package com.example.data.database.dao

import com.example.data.database.table.OrderTable
import com.example.data.database.table.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderEntity>(OrderTable)

    val userId by UserEntity referencedOn UserTable.id
    val orderDate by OrderTable.orderDate
    val amount by OrderTable.amount
    val status by OrderTable.status
}