package com.example.data.database.dao

import com.example.data.database.table.CartItemTable
import com.example.data.database.table.ProductTable
import com.example.domain.model.CartItem
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class CartItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CartItemEntity>(CartItemTable)

    var product by ProductEntity referencedOn ProductTable.id
    var quantity by CartItemTable.quantity
    var price by CartItemTable.price
    var userId by CartItemTable.userId
    var discountPrice by CartItemTable.discountPrice
    var discountPercentage by CartItemTable.discountPercentage
    var expiresAt by CartItemTable.expiresAt
    var status by CartItemTable.status

    private val isExpired: Boolean
        get() = LocalDateTime.now().isAfter(expiresAt)

    fun toCartItem() = CartItem(
        id = id.value,
        userId = userId.value,
        product = product.toProduct(),
        quantity = quantity,
        price = if (isExpired) product.price else price,
        discountPrice = if (isExpired) product.discountPrice else discountPrice,
        discountPercentage = if (isExpired) product.discountPercentage else discountPercentage,
        expiresAt = expiresAt,
        status = status
    )
}