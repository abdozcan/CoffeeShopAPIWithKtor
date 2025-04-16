package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.utils.Constant.ACCESS_TOKEN_EXPIRATION_MILLISECOND
import com.example.data.utils.Constant.RESET_PASSWORD_TOKEN_EXPIRATION_MILLISECOND
import java.util.*

class DefaultAuthManager(
    val audience: String,
    val issuer: String,
    val secret: String
) : AuthManager {
    override fun createToken(
        email: String,
        otp: String?,
        type: String
    ): String {
        val builder = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", email)
            .withClaim("type", type)
            .run { if (otp != null) withClaim("otp", otp) else this }
        if (type == "access")
            builder.withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MILLISECOND))
        else if (type == "reset")
            builder.withExpiresAt(Date(System.currentTimeMillis() + RESET_PASSWORD_TOKEN_EXPIRATION_MILLISECOND))
        return builder.sign(Algorithm.HMAC256(secret))
    }

    override fun verifyToken(
        token: String,
        email: String,
        otp: String?,
        type: String
    ): Result<Unit> = runCatching {
        JWT.require(Algorithm.HMAC256(secret))
            .withClaim("email", email)
            .withClaim("type", type)
            .run { if (otp != null) withClaim("otp", otp) else this }
            .build()
            .verify(token)
    }
}