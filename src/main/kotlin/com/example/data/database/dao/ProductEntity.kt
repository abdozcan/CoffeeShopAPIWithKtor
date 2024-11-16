package com.example.data.database.dao

import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.data.database.table.ReviewTable
import com.example.domain.model.Product
import com.example.domain.model.ProductInfo
import com.example.domain.model.SearchResultProductInfo
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and


class ProductEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductEntity>(ProductTable)

    var name by ProductTable.name
    var description by ProductTable.description
    var price by ProductTable.price
    var region by ProductTable.region
    var weight by ProductTable.weight
    var flavorProfile by ProductTable.flavorProfile
    var grindOption by ProductTable.grindOption
    var roastLevel by ProductTable.roastLevel
    var imageUrl by ProductTable.imageUrl
    var category by ProductTable.category
    var stockQuantity by ProductTable.stockQuantity
    var popularityRating by ProductTable.popularityRating
    var discountPrice by ProductTable.discountPrice
    var discountPercentage by ProductTable.discountPercentage
    var isBestseller by ProductTable.isBestseller

    val reviews by ReviewEntity referrersOn ReviewTable.productId

    fun toProduct(userId: Int?) = Product(
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
        isBestseller = isBestseller,
        isFavorite = userId?.let { isFavoriteProduct(userId) },
        reviews = reviews.map { it.toReview() }
    )

    fun toProductInfo() = ProductInfo(
        id = id.value,
        name = name,
        price = price,
        weight = weight,
        imageUrl = imageUrl,
        stockQuantity = stockQuantity,
        popularityRating = popularityRating,
        discountPrice = discountPrice,
        discountPercentage = discountPercentage
    )

    fun toSearchResultProductInfo() = SearchResultProductInfo(
        id = id.value,
        name = name,
        category = category,
        image = imageUrl
    )

    private fun isFavoriteProduct(userId: Int): Boolean = FavoriteEntity.find {
        (FavoriteTable.productId eq id.value) and (FavoriteTable.userId eq userId)
    }.none().not()

}