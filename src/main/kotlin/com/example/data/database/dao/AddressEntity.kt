package com.example.data.database.dao

import com.example.data.database.table.AddressTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AddressEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AddressEntity>(AddressTable)

    val name by AddressTable.name
    val userId by AddressTable.userId
    val address by AddressTable.address
}