package com.example.data.database.dao

import com.example.data.database.table.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    val name by UserTable.name
    val password by UserTable.password
    val email by UserTable.email
    val phone by UserTable.phone
    val address by UserTable.address
    val createdAt by UserTable.createdAt
}