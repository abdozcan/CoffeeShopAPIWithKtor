package com.example.domain.repository

import com.example.domain.model.Favorite

interface FavoriteRepository {
    suspend fun findAllByUserId(userId: Int): Result<List<Favorite>>
    suspend fun isFavorite(userId: Int, productId: Int): Result<Boolean>
    suspend fun add(userId: Int, productId: Int): Result<Unit>
    suspend fun delete(userId: Int, productId: Int): Result<Unit>
}