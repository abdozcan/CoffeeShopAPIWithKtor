package com.example.data.database.dao

import com.example.data.database.table.UserTable
import com.example.domain.model.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var name by UserTable.name
    var password by UserTable.password
    var email by UserTable.email
    var phone by UserTable.phone
    var defaultAddress: String? by UserTable.defaultAddress
    var createdAt by UserTable.createdAt

    fun toUser() = User(
        id = id.value,
        name = name,
        password = password,
        email = email,
        phone = phone,
        defaultAddress = defaultAddress,
        createdAt = createdAt
    )
}