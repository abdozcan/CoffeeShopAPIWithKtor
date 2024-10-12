package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object PromotionTable : IntIdTable("promotions") {
    val productId = integer("product_id").references(ProductTable.id)
    val discount = decimal("discount", 4, 2)
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
    val description = text("description")
}