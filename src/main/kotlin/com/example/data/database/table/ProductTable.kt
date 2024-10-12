package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object ProductTable : IntIdTable("products") {
    val name = text("name").uniqueIndex()
    val description = text("description")
    val price = float("price")  // REAL in SQLite corresponds to decimal in Exposed
    val region = text("region")
    val weight = integer("weight")
    val flavorProfile = text("flavor_profile")
    val grindOption = text("grind_option")
    val roastLevel = integer("roast_level")
    val imageUrl = text("image_url").uniqueIndex()
    val category = text("category")
    val stockQuantity = integer("stock_quantity")
    val popularityRating = float("popularity_rating")
    val discountPrice = float("discount_price").nullable()  // REAL can be nullable
    val discountPercentage = float("discount_percentage").nullable()  // Nullable discount_percentage
    val bestseller = bool("bestseller")
}