package com.example.data.repository

import com.example.data.database.dao.OrderEntity
import com.example.data.database.dao.OrderItemEntity
import com.example.data.database.dao.ProductEntity
import com.example.data.database.table.OrderItemTable
import com.example.data.database.table.OrderTable
import com.example.data.database.table.UserTable
import com.example.data.utils.OrderStatus
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.mapOrTrowIfEmpty
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Order
import com.example.domain.repository.OrderRepository
import com.example.domain.repository.ProductOfOrderItem
import com.example.domain.repository.RequestOrderedProduct
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.with
import java.time.LocalDateTime

class DefaultOrderRepository : OrderRepository {
    override suspend fun findAllByUserId(userId: Int): Result<List<Order>> = runCatching {
        withTransactionContext {
            OrderEntity.find {
                OrderTable.userId eq userId
            }.mapOrTrowIfEmpty {
                it.toOrder()
            }
        }
    }

    override suspend fun findById(id: Int): Result<Order> = runCatching {
        withTransactionContext {
            OrderEntity.findById(id).doOrThrowIfNull { it.toOrder() }
        }
    }

    override suspend fun findOrderItemProduct(orderId: Int): Result<List<ProductOfOrderItem>> = runCatching {
        withTransactionContext {
            OrderItemEntity.find {
                OrderItemTable.orderId eq orderId
            }.with(OrderItemEntity::product)
                .mapOrTrowIfEmpty {
                    ProductOfOrderItem(
                        productId = it.product.id.value,
                        name = it.product.name,
                        price = it.price,
                        quantity = it.quantity,
                        imageUrl = it.product.imageUrl,
                        category = it.product.category
                    )
                }
        }
    }

    override suspend fun add(
        userId: Int,
        shippingAddress: String,
        paymentMethod: String,
        orderDate: LocalDateTime,
        orderedProducts: List<RequestOrderedProduct>
    ): Result<Order> = runCatching {
        withTransactionContext {
            val order = OrderEntity.new {
                this.userId = EntityID(userId, UserTable)
                this.orderDate = orderDate
                this.totalAmount = orderedProducts.sumOf { it.price * it.quantity }
                this.status = OrderStatus.PENDING
                this.shippingAddress = shippingAddress
                this.paymentMethod = paymentMethod
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }

            orderedProducts.forEach { product ->
                OrderItemEntity.new {
                    this.orderId = order.id
                    this.product = ProductEntity.findById(product.id).doOrThrowIfNull { it }
                    this.quantity = product.quantity
                    this.price = product.price
                }
            }

            order.toOrder()
        }
    }

    override suspend fun cancel(orderId: Int): Result<Unit> = runCatching {
        withTransactionContext {
            OrderEntity.findByIdAndUpdate(orderId) {
                it.status = OrderStatus.CANCELLED
                it.updatedAt = LocalDateTime.now()
            }
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            OrderEntity.findById(id).doOrThrowIfNull { it.delete() }
        }
    }
}