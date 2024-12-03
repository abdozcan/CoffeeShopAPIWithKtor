package com.example.data.repository

import com.example.data.database.dao.PromoCodeEntity
import com.example.data.database.dao.PromoCodeUsageEntity
import com.example.data.database.table.PromoCodeTable
import com.example.data.database.table.PromoCodeUsageTable
import com.example.data.utils.withTransactionContext
import com.example.domain.model.PromoRespond
import com.example.domain.repository.PromoCodeRepository
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime

class DefaultPromoCodeRepository : PromoCodeRepository {
    override suspend fun validate(userId: Int, code: String, totalPrice: Double): Result<PromoRespond> = runCatching {
        withTransactionContext {
            PromoCodeEntity.find { PromoCodeTable.code eq code }.singleOrNull()?.let { promoCode ->
                validatePromoCode(promoCode, userId, totalPrice)
                promoCode.toPromoRespond()
            } ?: throw BadRequestException("Invalid code. Please try a valid one.")
        }
    }

    private fun validatePromoCode(promoCode: PromoCodeEntity, userId: Int, totalPrice: Double) {
        when {
            !promoCode.isActive -> throw BadRequestException("This code is inactive.")
            promoCode.usageCount >= promoCode.usageLimit -> throw BadRequestException("This code has been fully used.")
            promoCode.expirationDate <= LocalDateTime.now() -> throw BadRequestException("This code expired.")
            promoCode.minTotalPrice > totalPrice -> throw BadRequestException("Minimum total must be ${promoCode.minTotalPrice} for this code.")
            hasUserUsedCodeBefore(
                promoCode.id.value,
                userId
            ) -> throw BadRequestException("The code has already been used.")
        }
    }

    private fun hasUserUsedCodeBefore(promoCodeId: Int, userId: Int): Boolean {
        return PromoCodeUsageEntity.find {
            (PromoCodeUsageTable.promoCodeId eq promoCodeId) and (PromoCodeUsageTable.userId eq userId)
        }.any()
    }
}

