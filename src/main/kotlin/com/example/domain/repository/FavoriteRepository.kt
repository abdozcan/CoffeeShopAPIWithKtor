package com.example.domain.repository

import com.example.domain.model.ProductInfo
import com.example.domain.utils.ProductSortOption

interface FavoriteRepository {
    suspend fun findAllByUserId(
        userId: Int,
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>>
    suspend fun isFavorite(userId: Int, productId: Int): Result<Boolean>
    suspend fun add(userId: Int, productId: Int): Result<Unit>
    suspend fun delete(userId: Int, productId: Int): Result<Unit>
}