package com.example.domain.repository

import com.example.domain.model.PromoRespond

interface PromoCodeRepository {
    suspend fun validate(userId: Int, code: String, totalPrice: Double): Result<PromoRespond>
}