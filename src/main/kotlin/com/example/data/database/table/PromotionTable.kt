package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object PromotionTable : IntIdTable("promotions") {
    val productId = reference("product_id", foreign = ProductTable, onDelete = ReferenceOption.CASCADE)
    val discount = float("discount")
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
    val description = text("description")
}