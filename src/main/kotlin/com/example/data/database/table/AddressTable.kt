package com.example.data.database.table

import com.example.data.utils.Constant.ADDRESS_LENGTH
import com.example.data.utils.Constant.ADDRESS_NAME_LENGTH
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object AddressTable : IntIdTable("addresses") {
    val name = varchar("name", ADDRESS_NAME_LENGTH)
    val userId = reference("user_id", refColumn = UserTable.id, onDelete = ReferenceOption.CASCADE)
    val address = varchar("address", ADDRESS_LENGTH)
}