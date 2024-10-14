package com.example.data.database.dao

import com.example.data.database.table.CartItemTable
import com.example.data.database.table.CartTable
import com.example.domain.model.Cart
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CartEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CartEntity>(CartTable)

    var userId by CartTable.userId
    var createdAt by CartTable.createdAt
    var expiresAt by CartTable.expiresAt
    var status by CartTable.status

    val cartItems by CartItemEntity referrersOn CartItemTable.cartId

    fun toCart() = Cart(
        id = id.value,
        userId = userId.value,
        createdAt = createdAt,
        expiresAt = expiresAt,
        status = status,
        cartItems = cartItems.map { it.toCartItem() }
    )
}