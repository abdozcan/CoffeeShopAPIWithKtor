package com.example.data.database.dao

import com.example.data.database.table.*
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
    var address by UserTable.address
    var createdAt by UserTable.createdAt

    val favorites by FavoriteEntity referrersOn FavoriteTable.userId
    val orders by OrderEntity referrersOn OrderTable.userId
    val reviews by ReviewEntity referrersOn ReviewTable.userId
    val addresses by AddressEntity referrersOn AddressTable.userId

    fun toUser() = User(
        id = id.value,
        name = name,
        password = password,
        email = email,
        phone = phone,
        address = address,
        createdAt = createdAt
    )
}