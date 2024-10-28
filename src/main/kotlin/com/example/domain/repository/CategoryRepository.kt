package com.example.domain.repository

import com.example.domain.model.Category

interface CategoryRepository {
    suspend fun findAll(): Result<List<Category>>
}