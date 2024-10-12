package com.example.data.database.table

import com.example.data.model.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object OrderTable : IntIdTable("orders") {
    val userId = reference("user_id", foreign = UserTable)
    val orderDate = datetime("order_date")
    val amount = integer("amount")
    val status = text("status")
}