package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object OtpTable : IntIdTable("otps") {
    val email = varchar("email", 255)
    val otp = varchar("otp", 4)
    val token = text("token")
}