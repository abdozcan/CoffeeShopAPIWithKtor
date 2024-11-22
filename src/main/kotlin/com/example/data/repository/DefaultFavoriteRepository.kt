package com.example.data.repository

import com.example.data.database.dao.FavoriteEntity
import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.data.database.table.UserTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.mapOrTrowIfEmpty
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Favorite
import com.example.domain.repository.FavoriteRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and

class DefaultFavoriteRepository : FavoriteRepository {
    override suspend fun findAllByUserId(userId: Int): Result<List<Favorite>> = runCatching {
        withTransactionContext {
            FavoriteEntity.find {
                FavoriteTable.userId eq userId
            }.mapOrTrowIfEmpty {
                it.toFavorite()
            }
        }
    }

    override suspend fun isFavorite(userId: Int, productId: Int): Result<Boolean> = runCatching {
        withTransactionContext {
            FavoriteEntity
                .find {
                    (FavoriteTable.userId eq userId) and (FavoriteTable.productId eq productId)
                }.none().not()
        }
    }

    override suspend fun add(userId: Int, productId: Int): Result<Unit> = runCatching {
        withTransactionContext {
            FavoriteEntity.new {
                this.userId = EntityID(userId, UserTable)
                this.productId = EntityID(productId, ProductTable)
            }
        }
    }

    override suspend fun delete(userId: Int, productId: Int): Result<Unit> = runCatching {
        withTransactionContext {
            FavoriteEntity.find {
                (FavoriteTable.userId eq userId) and (FavoriteTable.productId eq productId)
            }.firstOrNull().doOrThrowIfNull { it.delete() }
        }
    }
}