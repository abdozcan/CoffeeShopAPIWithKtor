package com.example.data

import com.example.data.database.table.*
import com.example.data.model.*
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toCategory() = Category(
    id = this[CategoryTable.id].value,
    name = this[CategoryTable.name],
    description = this[CategoryTable.description]
)

fun ResultRow.toUser() = User(
    id = this[UserTable.id].value,
    name = this[UserTable.name],
    password = this[UserTable.password],
    email = this[UserTable.email],
    phone = this[UserTable.phone],
    address = this[UserTable.address],
    createdAt = this[UserTable.createdAt]
)

fun ResultRow.toProduct() = Product(
    id = this[ProductTable.id].value,
    name = this[ProductTable.name],
    description = this[ProductTable.description],
    price = this[ProductTable.price],
    region = this[ProductTable.region],
    weight = this[ProductTable.weight],
    flavorProfile = this[ProductTable.flavorProfile],
    grindOption = this[ProductTable.grindOption],
    roastLevel = this[ProductTable.roastLevel],
    imageUrl = this[ProductTable.imageUrl],
    category = this[ProductTable.category],
    stockQuantity = this[ProductTable.stockQuantity],
    popularityRating = this[ProductTable.popularityRating],
    discountPrice = this[ProductTable.discountPrice],
    discountPercentage = this[ProductTable.discountPercentage],
    bestseller = this[ProductTable.bestseller]
)

fun ResultRow.toOrder() = Order(
    id = this[OrderTable.id].value,
    userId = this[OrderTable.userId],
    orderDate = this[OrderTable.orderDate],
    amount = this[OrderTable.amount],
    status = this[OrderTable.status]
)

fun ResultRow.toOrderItem() = OrderItem(
    id = this[OrderItemEntity.id].value,
    orderId = this[OrderItemEntity.orderId],
    productId = this[OrderItemEntity.productId],
    quantity = this[OrderItemEntity.quantity],
    price = this[OrderItemEntity.price]
)

fun ResultRow.toPayment() = Payment(
    id = this[PaymentTable.id].value,
    orderId = this[PaymentTable.orderId],
    paymentDate = this[PaymentTable.paymentDate],
    amount = this[PaymentTable.amount],
    method = this[PaymentTable.method]
)

fun ResultRow.toFavorite() = Favorite(
    id = this[FavoriteTable.id].value,
    userId = this[FavoriteTable.userId],
    productId = this[FavoriteTable.productId]
)
