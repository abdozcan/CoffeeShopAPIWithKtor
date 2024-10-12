package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object CategoryTable : IntIdTable("categories") {
    val name = text("name").uniqueIndex()
    val description = text("description")
}
