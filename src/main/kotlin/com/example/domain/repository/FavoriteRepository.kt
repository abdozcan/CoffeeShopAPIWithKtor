package com.example.domain.repository

import com.example.domain.model.Favorite

interface FavoriteRepository {
    suspend fun isFavorite(userId: Int, productId: Int): Result<Favorite>
    suspend fun add(userId: Int, productId: Int): Result<Int>
    suspend fun delete(id: Int): Result<Unit>
}