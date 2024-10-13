package com.example.data.repository

import com.example.data.database.dao.FavoriteEntity
import com.example.data.database.dao.ProductEntity
import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.data.utils.flatMapOrTrowIfEmpty
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository

class DefaultProductRepository : ProductRepository {
    override suspend fun all(): Result<List<Product>> = runCatching {
        withTransactionContext {
            ProductEntity.all().map { it.toProduct() }
        }
    }

    override suspend fun findById(id: Int): Result<Product> = runCatching {
        withTransactionContext {
            ProductEntity.findById(id).doOrThrowIfNull { it.toProduct() }
        }
    }

    override suspend fun findByCategory(category: String): Result<List<Product>> = runCatching {
        withTransactionContext {
            ProductEntity.find { ProductTable.category eq category }.map { it.toProduct() }
        }
    }

    override suspend fun findBestsellers(): Result<List<Product>> = runCatching {
        withTransactionContext {
            ProductEntity.find { ProductTable.bestseller eq true }.map { it.toProduct() }
        }
    }

    override suspend fun findFavoritedProduct(userId: Int): Result<List<Product>> = runCatching {
        withTransactionContext {
            FavoriteEntity.find { FavoriteTable.userId eq userId }.flatMapOrTrowIfEmpty {
                it.products.map { productEntity -> productEntity.toProduct() }
            }
        }
    }
}


