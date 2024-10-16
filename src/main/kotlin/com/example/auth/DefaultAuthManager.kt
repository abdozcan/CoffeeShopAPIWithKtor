package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.utils.Constant.ACCESS_TOKEN_EXPIRATION_MILLISECOND
import java.util.*

class DefaultAuthManager(
    val audience: String,
    val issuer: String,
    val secret: String
) : AuthManager {
    override fun createToken(
        email: String,
        type: String
    ): String {
        val builder = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", email)
            .withClaim("type", type)
        if (type == "access")
            builder.withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MILLISECOND))
        return builder.sign(Algorithm.HMAC256(secret))
    }

    override fun verifyToken(
        token: String?,
        type: String
    ): Result<Unit> = runCatching {
        token ?: throw NullPointerException("Token is null.")
        val tokenType = JWT.require(Algorithm.HMAC256(secret))
            .build()
            .verify(token)
            .getClaim("type")
            .asString()
        if (type == tokenType) {
            return Result.success(Unit)
        } else throw IllegalArgumentException("Invalid token type.")
    }
}