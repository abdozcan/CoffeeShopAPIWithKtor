package com.example.domain.repository

import com.example.domain.model.Product
import com.example.domain.model.ProductInfo
import com.example.domain.model.SearchRequest
import com.example.domain.utils.ProductSortOption

interface ProductRepository {
    suspend fun all(limit: Int, offset: Long, sortOption: ProductSortOption): Result<List<ProductInfo>>
    suspend fun findById(id: Int): Result<Product>
    suspend fun findByCategory(
        category: String,
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>>

    suspend fun findBestsellers(limit: Int, offset: Long, sortOption: ProductSortOption): Result<List<ProductInfo>>
    suspend fun findFavoriteProduct(
        userId: Int,
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>>

    suspend fun search(request: SearchRequest): Result<List<ProductInfo>>
}