package com.example.domain.repository

interface OtpRepository {
    suspend fun find(email: String, otp: String): Result<String>
    suspend fun add(email: String, otp: String, token: String): Result<Unit>
    suspend fun update(email: String, otp: String, token: String): Result<Unit>
    suspend fun delete(email: String): Result<Unit>
}