package com.example.auth

interface AuthManager {
    fun createToken(email: String, otp: String? = null, type: String): String
    fun verifyToken(token: String, email: String, otp: String? = null, type: String): Result<Unit>
}