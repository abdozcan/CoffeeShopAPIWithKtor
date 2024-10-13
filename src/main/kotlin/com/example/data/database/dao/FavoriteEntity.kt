package com.example.data.database.dao

import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.domain.model.Favorite
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FavoriteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FavoriteEntity>(FavoriteTable)

    val userId by FavoriteTable.userId
    val productId by FavoriteTable.productId

    val products by ProductEntity referrersOn ProductTable.id

    fun toFavorite() = Favorite(
        id = id.value,
        userId = userId.value,
        productId = productId.value,
        products = products.map { it.toProduct() }
    )
}