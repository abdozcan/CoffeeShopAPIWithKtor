package com.example.data.repository

import com.example.data.database.dao.CartItemEntity
import com.example.data.database.dao.ProductEntity
import com.example.data.database.table.CartItemTable
import com.example.data.database.table.UserTable
import com.example.data.utils.CartStatus
import com.example.data.utils.Constant.CART_ITEM_EXPIRATION_HOURS
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.CartItem
import com.example.domain.repository.CartRepository
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class DefaultCartRepository : CartRepository {
    override suspend fun findAllByUserId(userId: Int): Result<List<CartItem>> = runCatching {
        withTransactionContext {
            CartItemEntity.find {
                CartItemTable.userId eq userId
            }.map {
                it.toCartItem()
            }
        }
    }

    override suspend fun add(userId: Int, productId: Int, quantity: Int): Result<Unit> = runCatching {
        withTransactionContext {
            CartItemEntity.new {
                this.userId = EntityID(userId, UserTable)
                this.product = ProductEntity.findById(productId).doOrThrowIfNull { it }
                this.quantity = quantity
                this.price = this.product.price
                this.discountPrice = this.product.discountPrice
                this.discountPercentage = this.product.discountPercentage
                this.expiresAt = LocalDateTime.now().plusHours(CART_ITEM_EXPIRATION_HOURS)
                this.status = CartStatus.ACTIVE
            }
        }
    }

    override suspend fun delete(ids: List<Int>): Result<Unit> = runCatching {
        withTransactionContext {
            CartItemEntity.forIds(ids).map { it.delete() }
        }
    }
}