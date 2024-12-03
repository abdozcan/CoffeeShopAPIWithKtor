package com.example.data.database.dao

import com.example.data.database.table.PromoCodeUsageTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PromoCodeUsageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PromoCodeUsageEntity>(PromoCodeUsageTable)

    var userId by PromoCodeUsageTable.userId
    var promoCodeId by PromoCodeUsageTable.promoCodeId
    var used by PromoCodeUsageTable.used
}