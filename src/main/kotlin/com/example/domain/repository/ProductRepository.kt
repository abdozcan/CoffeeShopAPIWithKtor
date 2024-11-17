package com.example.domain.repository

import com.example.domain.model.Product
import com.example.domain.model.ProductInfo
import com.example.domain.model.SearchRequest
import com.example.domain.model.SearchResultProductInfo
import com.example.domain.utils.ProductSortOption

interface ProductRepository {
    suspend fun all(limit: Int, offset: Long, sortOption: ProductSortOption): Result<List<ProductInfo>>
    suspend fun findById(id: Int, userId: Int?): Result<Product>
    suspend fun findByCategory(
        category: String,
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>>

    suspend fun findBestsellers(limit: Int, offset: Long, sortOption: ProductSortOption): Result<List<ProductInfo>>
    suspend fun findPopulars(limit: Int, offset: Long): Result<List<ProductInfo>>
    suspend fun findNewest(limit: Int, offset: Long): Result<List<ProductInfo>>
    suspend fun findSpecialOffers(limit: Int, offset: Long): Result<List<ProductInfo>>
    suspend fun findFavoriteProduct(
        userId: Int,
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>>

    suspend fun search(request: SearchRequest): Result<List<SearchResultProductInfo>>
}