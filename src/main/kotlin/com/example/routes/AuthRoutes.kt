package com.example.routes

import com.example.auth.AuthManager
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Application.authRoutes(repo: UserRepository, authManager: AuthManager) = routing {
    login(repo, authManager)
    register(repo, authManager)
}

fun Route.refreshToken(authManager: AuthManager) = post("/refresh_token") {

}

fun Route.login(repo: UserRepository, authManager: AuthManager) = post("/login") {
    runCatching {
        call.receive<Credential>()
    }.onSuccess { credential ->
        if (repo.isIncorrectCredential(credential))
            return@post call.respond(HttpStatusCode.Conflict, "Invalid email or password")

        call.respond(
            HttpStatusCode.OK,
            Tokens(
                accessToken = authManager.createToken(credential.email, "access"),
                refreshToken = authManager.createToken(credential.email, "refresh")
            )
        )
    }.onFailure {
        call.respond(HttpStatusCode.BadRequest, "Invalid request body.")
    }
}

fun Route.register(repo: UserRepository, authManager: AuthManager) = post("/register") {
    runCatching {
        call.receive<User>()
    }.onSuccess { user ->
        if (repo.isEmailUsed(user.email))
            return@post call.respond(HttpStatusCode.Conflict, "Email already used.")
        if (repo.isPhoneUsed(user.phone))
            return@post call.respond(HttpStatusCode.Conflict, "Phone already used.")

        repo.add(user.name, user.password, user.email, user.phone, user.defaultAddress)
            .onSuccess {
                call.respond(
                    HttpStatusCode.Created,
                    Tokens(
                        accessToken = authManager.createToken(user.email, "access"),
                        refreshToken = authManager.createToken(user.email, "refresh")
                    )
                )
            }.onFailure { exception ->
                call.respond(HttpStatusCode.InternalServerError, exception.message.toString())
            }
    }.onFailure {
        call.respond(HttpStatusCode.BadRequest, "Invalid request body.")
    }
}

@Serializable
data class Credential(
    val email: String,
    val password: String
)

@Serializable
data class Tokens(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String
)
