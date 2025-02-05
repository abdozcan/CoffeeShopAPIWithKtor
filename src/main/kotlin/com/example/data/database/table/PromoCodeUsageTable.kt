package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object PromoCodeUsageTable : IntIdTable("promo_code_usages") {
    val userId = reference(name = "user_id", foreign = UserTable)
    val promoCodeId = reference(name = "promo_code_id", foreign = PromoCodeTable)
    val used = bool(name = "used")
    val orderId = reference("order_id", foreign = OrderTable)
}