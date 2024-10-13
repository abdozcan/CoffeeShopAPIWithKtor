package com.example.data.database.dao

import com.example.data.database.table.CategoryTable
import com.example.domain.model.Category
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CategoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryEntity>(CategoryTable)

    val name by CategoryTable.name
    val description by CategoryTable.description

    fun toCategory() = Category(
        id = id.value,
        name = name,
        description = description
    )
}