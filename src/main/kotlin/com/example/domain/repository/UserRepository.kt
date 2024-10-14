package com.example.domain.repository

import com.example.domain.model.User

interface UserRepository {
    suspend fun findByEmail(email: String): Result<User>
    suspend fun add(name: String, password: String, email: String, phone: String, address: String = ""): Result<User>
    suspend fun delete(id: Int): Result<Unit>
    suspend fun selectDefaultAddress(userId: Int, address: String): Result<User>
}