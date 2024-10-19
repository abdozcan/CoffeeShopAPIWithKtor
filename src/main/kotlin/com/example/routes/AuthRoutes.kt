package com.example.routes

import com.example.auth.AuthManager
import com.example.auth.model.Credential
import com.example.auth.model.RefreshTokenRequest
import com.example.auth.model.Tokens
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoutes(repo: UserRepository, authManager: AuthManager) = routing {
    refreshToken(authManager)
    login(repo, authManager)
    register(repo, authManager)
}

fun Route.refreshToken(authManager: AuthManager) = authenticate {
    post("/refresh_token") {
        call.receive<RefreshTokenRequest>().let { refreshTokenRequest ->
            authManager.verifyToken(
                token = refreshTokenRequest.refreshToken,
                type = "refresh"
            ).getOrThrow()
            call.respond(
                HttpStatusCode.OK,
                Tokens(
                    accessToken = authManager.createToken(email = refreshTokenRequest.email, type = "access"),
                    refreshToken = authManager.createToken(email = refreshTokenRequest.email, type = "refresh")
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
    call.receive<User>().let { user ->
        if (repo.isEmailUsed(user.email))
            return@post call.respond(HttpStatusCode.Conflict, "Email already used.")
        if (repo.isPhoneUsed(user.phone))
            return@post call.respond(HttpStatusCode.Conflict, "Phone already used.")

        repo.add(
            user.name,
            user.password,
            user.email,
            user.phone,
            user.defaultAddress
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
