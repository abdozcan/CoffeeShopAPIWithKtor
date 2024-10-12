package com.example.data.database.dao

import com.example.data.database.table.FavoriteTable
import com.example.data.database.table.ProductTable
import com.example.data.database.table.UserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FavoriteEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FavoriteEntity>(FavoriteTable)

    val userId by UserEntity referencedOn UserTable.id
    val productId by ProductEntity referencedOn ProductTable.id
}