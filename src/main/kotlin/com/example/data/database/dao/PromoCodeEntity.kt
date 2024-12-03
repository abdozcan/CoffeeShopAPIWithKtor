package com.example.data.database.dao

import com.example.data.database.table.PromoCodeTable
import com.example.domain.model.PromoRespond
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PromoCodeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PromoCodeEntity>(PromoCodeTable)

    var code by PromoCodeTable.code
    var isActive by PromoCodeTable.isActive
    var discount by PromoCodeTable.discount
    var discountType by PromoCodeTable.discountType
    var minTotalPrice by PromoCodeTable.minTotalPrice
    var usageLimit by PromoCodeTable.usageLimit
    var usageCount by PromoCodeTable.usageCount
    var expirationDate by PromoCodeTable.expirationDate

    fun toPromoRespond() = PromoRespond(
        promoCodeId = id.value,
        discount = discount,
        discountType = discountType,
        minTotalPrice = minTotalPrice
    )
}