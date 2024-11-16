package com.example.routes

import com.example.auth.AuthManager
import com.example.auth.model.Credential
import com.example.auth.model.RefreshToken
import com.example.auth.model.Tokens
import com.example.domain.model.RegisterUser
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.authRoutes(repo: UserRepository, authManager: AuthManager) = routing {
    route("auth") {
        refreshToken(authManager)
        login(repo, authManager)
        register(repo, authManager)
    }
}

fun Route.refreshToken(authManager: AuthManager) = authenticate {
    post("/refresh_token") {
        call.receive<RefreshToken>().let { refreshToken ->
            val email: String = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()!!

            authManager.verifyToken(
                token = refreshToken.refreshToken,
                email = email,
                type = "refresh"
            ).getOrThrow()
            call.respond(
                HttpStatusCode.OK,
                Tokens(
                    accessToken = authManager.createToken(email = email, type = "access"),
                    refreshToken = authManager.createToken(email = email, type = "refresh")
                )
            )
        }
    }
}

fun Route.login(repo: UserRepository, authManager: AuthManager) = post("/login") {
    call.receive<Credential>().let { credential ->
        if (repo.isIncorrectCredential(credential))
            return@post call.respond(HttpStatusCode.Conflict, "Invalid email or password")

        call.respond(
            HttpStatusCode.OK,
            Tokens(
                accessToken = authManager.createToken(credential.email, "access"),
                refreshToken = authManager.createToken(credential.email, "refresh")
            )
        )
    }
}

fun Route.register(repo: UserRepository, authManager: AuthManager) = post("/register") {
    call.receive<RegisterUser>().let { user ->
        if (repo.isEmailUsed(user.email))
            return@post call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(error = "Email already used", type = RegisterErrorType.EMAIL_USED)
            )
        if (repo.isPhoneUsed(user.phone))
            return@post call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(error = "Phone already used", type = RegisterErrorType.PHONE_USED)
            )

        repo.add(
            user.name,
            user.password,
            user.email,
            user.phone
        ).getOrThrow().let { addedUser ->
            call.respond(
                HttpStatusCode.Created,
                Tokens(
                    accessToken = authManager.createToken(addedUser.email, "access"),
                    refreshToken = authManager.createToken(addedUser.email, "refresh")
                )
            )
        }
    }
}

@Serializable
private data class ErrorResponse(
    val error: String,
    val type: RegisterErrorType,
)

private enum class RegisterErrorType {
    EMAIL_USED,
    PHONE_USED
}