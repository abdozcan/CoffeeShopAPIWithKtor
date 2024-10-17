package com.example.data.database.table

import com.example.data.utils.Constant.ADDRESS_LENGTH
import com.example.data.utils.Constant.ADDRESS_NAME_LENGTH
import org.jetbrains.exposed.dao.id.IntIdTable

object AddressTable : IntIdTable("addresses") {
    val name = varchar("address", ADDRESS_NAME_LENGTH).uniqueIndex()
    val userId = reference("user_id", foreign = UserTable)
    val address = varchar("address", ADDRESS_LENGTH)
}