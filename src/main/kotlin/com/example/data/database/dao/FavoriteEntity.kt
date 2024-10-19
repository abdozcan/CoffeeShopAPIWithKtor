package com.example.data.database.dao

import com.example.data.database.table.FavoriteTable
import com.example.domain.model.Favorite
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FavoriteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FavoriteEntity>(FavoriteTable)

    var userId by FavoriteTable.userId
    var productId by FavoriteTable.productId

    fun toFavorite() = Favorite(
        id = id.value,
        userId = userId.value,
        productId = productId.value
    )
}