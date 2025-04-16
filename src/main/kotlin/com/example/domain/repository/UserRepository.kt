package com.example.domain.repository

import com.example.auth.model.Credential
import com.example.domain.model.User

interface UserRepository {
    suspend fun findByEmail(email: String): Result<User>
    suspend fun findById(id: Int): Result<User>
    suspend fun isEmailUsed(email: String): Boolean
    suspend fun isPhoneUsed(phone: String): Boolean
    suspend fun isIncorrectCredential(credential: Credential): Boolean
    suspend fun add(
        name: String,
        password: String,
        email: String,
        phone: String
    ): Result<User>
    suspend fun edit(user: User): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
    suspend fun changePassword(userId: Int, oldPassword: String, newPassword: String): Result<Unit>
    suspend fun resetPassword(email: String, password: String): Result<Unit>
}