package com.example.data.database.dao

import com.example.data.database.table.FaqTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FaqEntity(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, FaqEntity>(FaqTable)

}