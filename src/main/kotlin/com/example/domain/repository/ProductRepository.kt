package com.example.domain.repository

import com.example.domain.model.Product
import com.example.domain.model.SearchRequest

interface ProductRepository {
    suspend fun all(limit: Int, offset: Long): Result<List<Product>>
    suspend fun findById(id: Int, limit: Int, offset: Long): Result<Product>


    /**
     * @throws NotFoundException
     */
    suspend fun findByCategory(category: String, limit: Int, offset: Long): Result<List<Product>>
    suspend fun findBestsellers(limit: Int, offset: Long): Result<List<Product>>
    suspend fun findFavoriteProduct(userId: Int, limit: Int, offset: Long): Result<List<Product>>
    suspend fun search(request: SearchRequest): Result<List<Product>>
}