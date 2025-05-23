package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductTable : IntIdTable("products") {
    val name = text("name").uniqueIndex()
    val description = text("description")
    val price = double("price")
    val region = text("region")
    val weight = integer("weight")
    val flavorProfile = text("flavor_profile")
    val grindOption = text("grind_option")
    val roastLevel = integer("roast_level")
    val imageUrl = text("image_url")
    val category = reference("category", refColumn = CategoryTable.name)
    val stockQuantity = integer("stock_quantity")
    val popularityRating = float("popularity_rating")
    val discountPrice = double("discount_price").nullable()
    val discountPercentage = double("discount_percentage").nullable()
    val isBestseller = bool("is_bestseller")
}