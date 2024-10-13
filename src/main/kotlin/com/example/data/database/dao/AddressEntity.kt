package com.example.data.database.dao

import com.example.data.database.table.AddressTable
import com.example.domain.model.Address
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AddressEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AddressEntity>(AddressTable)

    var name by AddressTable.name
    var userId by AddressTable.userId
    var address by AddressTable.address

    fun toAddress() = Address(
        id = id.value,
        name = name,
        userId = userId.value,
        address = address
    )
}