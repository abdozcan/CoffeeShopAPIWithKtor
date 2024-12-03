package com.example.data.database.table

import com.example.data.utils.DiscountType
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object PromoCodeTable : IntIdTable("promo_codes") {
    val code = varchar(name = "code", length = 55).uniqueIndex()
    val isActive = bool(name = "is_active")
    val minTotalPrice = double(name = "min_total_price")
    val discount = float(name = "discount")
    val discountType = enumeration<DiscountType>(name = "discount_type")
    val expirationDate = datetime(name = "expiration_date")
    val usageLimit = integer(name = "usage_limit")
    val usageCount = integer(name = "usage_count")
}