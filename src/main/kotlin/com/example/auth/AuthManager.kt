package com.example.auth

interface AuthManager {
    fun createToken(email: String, type: String): String
    fun verifyToken(token: String?, type: String): Result<Unit>
}