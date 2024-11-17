package com.example.data.repository

import com.example.data.database.dao.FavoriteEntity
import com.example.data.database.dao.ProductEntity
import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.mapOrTrowIfEmpty
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Product
import com.example.domain.model.ProductInfo
import com.example.domain.model.SearchRequest
import com.example.domain.model.SearchResultProductInfo
import com.example.domain.repository.ProductRepository
import com.example.domain.utils.ProductSortOption
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.coalesce
import org.jetbrains.exposed.sql.and

class DefaultProductRepository : ProductRepository {
    override suspend fun all(limit: Int, offset: Long, sortOption: ProductSortOption): Result<List<ProductInfo>> =
        runCatching {
            withTransactionContext {
                ProductEntity.all().limit(limit, offset).sortBy(sortOption).mapOrTrowIfEmpty { it.toProductInfo() }
            }
        }

    override suspend fun findById(id: Int, userId: Int?): Result<Product> = runCatching {
        withTransactionContext {
            ProductEntity.findById(id).doOrThrowIfNull { it.toProduct(userId) }
        }
    }

    override suspend fun findByCategory(
        category: String,
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>> =
        runCatching {
            withTransactionContext {
                ProductEntity.find {
                    ProductTable.category eq category
                }.limit(limit, offset).sortBy(sortOption).mapOrTrowIfEmpty {
                    it.toProductInfo()
                }
            }
        }

    override suspend fun findBestsellers(
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>> =
        runCatching {
            withTransactionContext {
                ProductEntity.find {
                    ProductTable.isBestseller eq true
                }.limit(limit, offset).sortBy(sortOption).mapOrTrowIfEmpty {
                    it.toProductInfo()
                }
            }
        }

    override suspend fun findPopulars(
        limit: Int,
        offset: Long
    ): Result<List<ProductInfo>> = runCatching {
        withTransactionContext {
            ProductEntity.all()
                .orderBy(ProductTable.popularityRating to SortOrder.DESC)
                .limit(limit, offset)
                .mapOrTrowIfEmpty {
                    it.toProductInfo()
                }
        }
    }

    override suspend fun findNewest(
        limit: Int,
        offset: Long
    ): Result<List<ProductInfo>> = runCatching {
        withTransactionContext {
            ProductEntity.all()
                .orderBy(ProductTable.id to SortOrder.DESC)
                .limit(limit, offset)
                .mapOrTrowIfEmpty {
                    it.toProductInfo()
                }
        }
    }

    override suspend fun findSpecialOffers(limit: Int, offset: Long): Result<List<ProductInfo>> = runCatching {
        withTransactionContext {
            ProductEntity.find {
                ProductTable.discountPrice neq null
            }.limit(limit, offset).mapOrTrowIfEmpty {
                it.toProductInfo()
            }
        }
    }

    override suspend fun findFavoriteProduct(
        userId: Int,
        limit: Int,
        offset: Long,
        sortOption: ProductSortOption
    ): Result<List<ProductInfo>> =
        runCatching {
            withTransactionContext {
                FavoriteEntity.find { FavoriteTable.userId eq userId }.limit(limit, offset).sortBy(sortOption)
                    .mapOrTrowIfEmpty { favoriteProduct ->
                        ProductEntity.findById(favoriteProduct.productId.value).doOrThrowIfNull { product ->
                            product.toProductInfo()
                        }
                    }
            }
        }

    override suspend fun search(request: SearchRequest): Result<List<SearchResultProductInfo>> = runCatching {
        val offset = (request.page - 1) * request.limit

        withTransactionContext {
            ProductEntity.find {
                val op = if (request.category != null)
                    (ProductTable.name like "%${request.name}%") and (ProductTable.category eq request.category)
                else (ProductTable.name like "%${request.name}%")

                op.and(ProductTable.price greaterEq request.minPrice)
                    .and(ProductTable.price lessEq request.maxPrice)
            }.limit(request.limit, offset)
                .sortBy(request.sort)
                .mapOrTrowIfEmpty { it.toSearchResultProductInfo() }
        }
    }
}

private fun <T> SizedIterable<T>.sortBy(sort: ProductSortOption): SizedIterable<T> =
    orderBy(
        when (sort) {
            ProductSortOption.PRICE_ASC -> coalesce(
                ProductTable.discountPrice,
                ProductTable.price
            ) to SortOrder.ASC

            ProductSortOption.PRICE_DESC -> coalesce(
                ProductTable.discountPrice,
                ProductTable.price
            ) to SortOrder.DESC

            ProductSortOption.POPULARITY -> ProductTable.popularityRating to SortOrder.DESC
            ProductSortOption.DATE_DESC -> ProductTable.id to SortOrder.DESC
            ProductSortOption.DATE_ASC -> ProductTable.id to SortOrder.ASC
        }
    )


