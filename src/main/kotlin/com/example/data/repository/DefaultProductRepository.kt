package com.example.data.repository

import com.example.data.database.dao.FavoriteEntity
import com.example.data.database.dao.ProductEntity
import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.mapOrTrowIfEmpty
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Product
import com.example.domain.model.SearchRequest
import com.example.domain.repository.ProductRepository
import com.example.domain.utils.SortOption
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and

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

    override suspend fun search(request: SearchRequest): Result<List<Product>> = runCatching {
        val offset = (request.page - 1) * request.limit

        withTransactionContext {
            ProductEntity.find {
                val op = if (request.category != null)
                    (ProductTable.name like "%${request.name}%") and (ProductTable.category eq request.category)
                else (ProductTable.name like "%${request.name}%")

                op.and(ProductTable.price greaterEq request.minPrice)
                    .and(ProductTable.price lessEq request.maxPrice)
            }.limit(request.limit, offset)
                .orderBy(
                    when (request.sort) {
                        SortOption.PRICE_ASC -> ProductTable.price to SortOrder.ASC
                        SortOption.PRICE_DESC -> ProductTable.price to SortOrder.DESC
                        SortOption.POPULARITY -> ProductTable.popularityRating to SortOrder.DESC
                    }
                ).mapOrTrowIfEmpty { it.toProduct() }
        }
    }
}


