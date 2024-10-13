package com.example.data.database.dao

import com.example.data.database.table.PromotionTable
import com.example.domain.model.Promotion
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PromotionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PromotionEntity>(PromotionTable)

    var productId by PromotionTable.productId
    var discount by PromotionTable.discount
    var startDate by PromotionTable.startDate
    var endDate by PromotionTable.endDate
    var description by PromotionTable.description

    fun toPromotion() = Promotion(
        id = id.value,
        productId = productId.value,
        discount = discount,
        startDate = startDate,
        endDate = endDate,
        description = description
    )
}