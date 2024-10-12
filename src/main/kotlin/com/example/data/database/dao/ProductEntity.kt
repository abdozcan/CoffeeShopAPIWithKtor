package com.example.data.database.dao

import com.example.data.database.table.ProductTable
import com.example.data.database.table.ReviewTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class ProductEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductEntity>(ProductTable)

    val name by ProductTable.name
    val description by ProductTable.description
    val price by ProductTable.price
    val region by ProductTable.region
    val weight by ProductTable.weight
    val flavorProfile by ProductTable.flavorProfile
    val grindOption by ProductTable.grindOption
    val roastLevel by ProductTable.roastLevel
    val imageUrl by ProductTable.imageUrl
    val category by ProductTable.category
    val stockQuantity by ProductTable.stockQuantity
    val popularityRating by ProductTable.popularityRating
    val discountPrice by ProductTable.discountPrice
    val discountPercentage by ProductTable.discountPercentage
    val bestseller by ProductTable.bestseller

    val reviews by ReviewEntity referrersOn ReviewTable.productId
}