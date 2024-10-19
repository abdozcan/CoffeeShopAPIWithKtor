package com.example.data.repository

import com.example.data.database.dao.FavoriteEntity
import com.example.data.database.dao.ProductEntity
import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.mapOrTrowIfEmpty
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository

class DefaultProductRepository : ProductRepository {
    override suspend fun all(): Result<List<Product>> = runCatching {
        withTransactionContext {
            ProductEntity.all().mapOrTrowIfEmpty { it.toProduct() }
        }
    }

    /**
     * @throws NotFoundException
     */
    override suspend fun findById(id: Int): Result<Product> = runCatching {
        withTransactionContext {
            ProductEntity.findById(id).doOrThrowIfNull { it.toProduct() }
        }
    }

    /**
     * @throws NoSuchElementException
     */
    override suspend fun findByCategory(category: String): Result<List<Product>> = runCatching {
        withTransactionContext {
            ProductEntity.find {
                ProductTable.category eq category
            }.mapOrTrowIfEmpty {
                it.toProduct()
            }
        }
    }

    override suspend fun findBestsellers(): Result<List<Product>> = runCatching {
        withTransactionContext {
            ProductEntity.find {
                ProductTable.bestseller eq true
            }.mapOrTrowIfEmpty {
                it.toProduct()
            }
        }
    }

    override suspend fun findFavoriteProduct(userId: Int): Result<List<Product>> = runCatching {
        withTransactionContext {
            FavoriteEntity.find { FavoriteTable.userId eq userId }.mapOrTrowIfEmpty { favoriteProduct ->
                ProductEntity.findById(favoriteProduct.productId.value).doOrThrowIfNull { product ->
                    product.toProduct()
                }
            }
        }
    }
}


