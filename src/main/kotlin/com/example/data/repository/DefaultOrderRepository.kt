package com.example.data.repository

import com.example.data.database.dao.*
import com.example.data.database.table.*
import com.example.data.utils.CartStatus
import com.example.data.utils.OrderStatus
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Order
import com.example.domain.model.PaymentStatus
import com.example.domain.repository.OrderRepository
import com.example.domain.repository.ProductOfOrderItem
import com.example.domain.repository.RequestOrderedProduct
import com.example.domain.utils.OrderSortOption
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime

class DefaultOrderRepository : OrderRepository {
    override suspend fun findAllByUserId(
        userId: Int,
        limit: Int,
        offset: Long,
        sortOption: OrderSortOption
    ): Result<List<Order>> = runCatching {
        withTransactionContext {
            OrderEntity.find { OrderTable.userId eq userId }
                .limit(limit, offset)
                .sortBy(sortOption)
                .map {
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
                .map {
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
        totalAmount: Double,
        promoCodeId: Int?,
        orderedProducts: List<RequestOrderedProduct>
    ): Result<Order> = runCatching {
        withTransactionContext {
            val order = OrderEntity.new {
                this.userId = EntityID(userId, UserTable)
                this.orderDate = orderDate
                this.totalAmount = totalAmount
                this.status = OrderStatus.PENDING
                this.shippingAddress = shippingAddress
                this.paymentMethod = paymentMethod
                this.promoCodeId = promoCodeId?.let { EntityID(it, PromoCodeTable) }
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }

            promoCodeId?.let { promoCodeId ->
                PromoCodeUsageEntity.new {
                    this.userId = EntityID(userId, UserTable)
                    this.orderId = order.id
                    this.used = true
                    this.promoCodeId = EntityID(promoCodeId, PromoCodeTable)
                }
            }

            orderedProducts.forEach { product ->
                OrderItemEntity.new {
                    this.orderId = order.id
                    this.product = ProductEntity.findByIdAndUpdate(product.id) {
                        it.stockQuantity -= product.quantity
                    }.doOrThrowIfNull { it }
                    this.quantity = product.quantity
                    this.price = product.price
                }
                setCartItemStatusCompleted(userId, product.id)
            }

            PaymentEntity.new {
                this.orderId = order.id
                this.paymentDate = LocalDateTime.now()
                this.amount = totalAmount
                this.method = paymentMethod
                this.status = PaymentStatus.PENDING
                this.transactionId = "BANK_TRANSACTION_ID"
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }

            order.toOrder()
        }
    }

    override suspend fun cancel(orderId: Int): Result<Unit> = runCatching {
        withTransactionContext {
            OrderEntity.findByIdAndUpdate(orderId) {
                it.status = OrderStatus.CANCELLED
                it.updatedAt = LocalDateTime.now()
            }?.let { order ->
                order.orderItems.forEach { orderItem ->
                    ProductEntity.findByIdAndUpdate(orderItem.product.id.value) {
                        it.stockQuantity += orderItem.quantity
                    }
                }
            }
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            OrderEntity.findById(id).doOrThrowIfNull { it.delete() }
        }
    }

    private fun setCartItemStatusCompleted(userId: Int, productId: Int) {
        CartItemEntity.find {
            (CartItemTable.userId eq userId) and (CartItemTable.productId eq productId) and (CartItemTable.status eq CartStatus.ACTIVE)
        }.firstOrNull().doOrThrowIfNull {
            it.status = CartStatus.COMPLETED
        }
    }
}

fun <T> SizedIterable<T>.sortBy(sort: OrderSortOption): SizedIterable<T> =
    orderBy(
        when (sort) {
            OrderSortOption.DATE_DESC -> OrderTable.orderDate to SortOrder.DESC
            OrderSortOption.DATE_ASC -> OrderTable.orderDate to SortOrder.ASC
            OrderSortOption.STATUS -> OrderTable.status to SortOrder.ASC
        }
    )