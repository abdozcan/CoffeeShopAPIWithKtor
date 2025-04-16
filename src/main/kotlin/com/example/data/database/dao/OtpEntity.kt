package com.example.data.database.dao

import com.example.data.database.table.OtpTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OtpEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OtpEntity>(OtpTable)

    var email by OtpTable.email
    var otp by OtpTable.otp
    var token by OtpTable.token
}