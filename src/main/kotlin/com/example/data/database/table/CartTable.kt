package com.example.data.database.table

import com.example.data.utils.CartStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object CartTable : IntIdTable("cart") {
    val userId = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val expiresAt = datetime("expires_at").nullable()
    val status = enumeration<CartStatus>("status")
}