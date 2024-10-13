package com.example.data.database.dao

import com.example.data.database.table.PromotionTable
import com.example.domain.model.Promotion
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PromotionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PromotionEntity>(PromotionTable)

    val productId by PromotionTable.productId
    val discount by PromotionTable.discount
    val startDate by PromotionTable.startDate
    val endDate by PromotionTable.endDate
    val description by PromotionTable.description

    fun toPromotion() = Promotion(
        id = id.value,
        productId = productId.value,
        discount = discount,
        startDate = startDate,
        endDate = endDate,
        description = description
    )
}