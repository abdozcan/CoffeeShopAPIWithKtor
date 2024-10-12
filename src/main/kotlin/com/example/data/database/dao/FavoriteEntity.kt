package com.example.data.database.dao

import com.example.data.database.table.FavoriteTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FavoriteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FavoriteEntity>(FavoriteTable)

    val userId by FavoriteTable.userId
    val productId by FavoriteTable.productId
}