package com.example.data

import com.example.data.database.dao.*
import com.example.domain.model.*

fun CategoryEntity.toCategory() = Category(
    id = id.value,
    name = name,
    description = description
)

fun UserEntity.toUser() = User(
    id = id.value,
    name = name,
    password = password,
    email = email,
    phone = phone,
    address = address,
    createdAt = createdAt
)

fun ProductEntity.toProduct() = Product(
    id = id.value,
    name = name,
    description = description,
    price = price,
    region = region,
    weight = weight,
    flavorProfile = flavorProfile,
    grindOption = grindOption,
    roastLevel = roastLevel,
    imageUrl = imageUrl,
    category = category,
    stockQuantity = stockQuantity,
    popularityRating = popularityRating,
    discountPrice = discountPrice,
    discountPercentage = discountPercentage,
    bestseller = bestseller
)

fun OrderEntity.toOrder() = Order(
    id = id.value,
    userId = userId.value,
    orderDate = orderDate,
    amount = amount,
    status = status
)

fun OrderItemEntity.toOrderItem() = OrderItem(
    id = id.value,
    orderId = orderId.value,
    productId = productId.value,
    quantity = quantity,
    price = price
)

fun PaymentEntity.toPayment() = Payment(
    id = id.value,
    orderId = orderId.value,
    paymentDate = paymentDate,
    amount = amount,
    method = method
)

fun FavoriteEntity.toFavorite() = Favorite(
    id = id.value,
    userId = userId.value,
    productId = productId.value
)

fun AddressEntity.toAddress() = Address(
    id = id.value,
    name = name,
    userId = userId.value,
    address = address
)
