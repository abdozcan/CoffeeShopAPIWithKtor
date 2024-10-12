package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object AddressTable : IntIdTable("addresses") {
    val name = varchar("address", 50).uniqueIndex()
    val userId = reference("user_id", foreign = UserTable)
    val address = varchar("address", 255)
}