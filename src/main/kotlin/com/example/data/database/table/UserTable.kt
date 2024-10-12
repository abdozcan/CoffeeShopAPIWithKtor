package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : IntIdTable("users") {
    val name = varchar("name", 255)
    val password = varchar("password", 255)
    val email = varchar("email", 255).uniqueIndex()
    val phone = varchar("phone", 20)
    val address = varchar("address", 255)
    val createdAt = datetime("created_at")
}
