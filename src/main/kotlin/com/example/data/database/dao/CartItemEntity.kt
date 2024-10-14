package com.example.data.database.dao

import com.example.data.database.table.CartItemTable
import com.example.domain.model.CartItem
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CartItemEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CartItemEntity>(CartItemTable)

    var productId by CartItemTable.productId
    var quantity by CartItemTable.quantity
    var price by CartItemTable.price
    var cartId by CartItemTable.cartId
    var discountPrice by CartItemTable.discountPrice
    var discountPercentage by CartItemTable.discountPercentage

    fun toCartItem() = CartItem(
        id = id.value,
        cartId = cartId.value,
        productId = productId.value,
        quantity = quantity,
        price = price,
        discountPrice = discountPrice,
        discountPercentage = discountPercentage
    )
}