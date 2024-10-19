package com.example.domain.repository

import com.example.domain.model.Product

interface ProductRepository {
    suspend fun all(): Result<List<Product>>
    suspend fun findById(id: Int): Result<Product>


    /**
     * @throws NotFoundException
     */
    suspend fun findByCategory(category: String): Result<List<Product>>
    suspend fun findBestsellers(): Result<List<Product>>
    suspend fun findFavoriteProduct(userId: Int): Result<List<Product>>
}