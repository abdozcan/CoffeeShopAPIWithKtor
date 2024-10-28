package com.example.data.repository

import com.example.data.database.dao.CategoryEntity
import com.example.data.utils.mapOrTrowIfEmpty
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Category
import com.example.domain.repository.CategoryRepository

class DefaultCategoryRepository : CategoryRepository {
    override suspend fun findAll(): Result<List<Category>> = runCatching {
        withTransactionContext {
            CategoryEntity.all().mapOrTrowIfEmpty { it.toCategory() }
        }
    }
}