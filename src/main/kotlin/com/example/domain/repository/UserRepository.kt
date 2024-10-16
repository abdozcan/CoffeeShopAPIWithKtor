package com.example.domain.repository

import com.example.domain.model.User
import com.example.routes.Credential

interface UserRepository {
    suspend fun findByEmail(email: String): Result<User>
    suspend fun isEmailUsed(email: String): Boolean
    suspend fun isPhoneUsed(phone: String): Boolean
    suspend fun isIncorrectCredential(credential: Credential): Boolean
    suspend fun add(
        name: String,
        password: String,
        email: String,
        phone: String,
        defaultAddress: String = ""
    ): Result<User>
    suspend fun delete(id: Int): Result<Unit>
    suspend fun selectDefaultAddress(userId: Int, address: String): Result<User>
}