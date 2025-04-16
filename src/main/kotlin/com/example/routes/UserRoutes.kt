package com.example.routes

import com.example.domain.model.ChangePasswordRequest
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import com.example.routes.utils.getAuthenticatedUsersId
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Tag(["User"])
fun Application.userRoutes(userRepo: UserRepository, otpRepo: OtpRepository) = routing {
    authenticate {
        getInfo(userRepo)
        getName(userRepo)
        getByEmail(userRepo)
        getById(userRepo)
        edit(userRepo)
        delete(userRepo)
        changePassword(userRepo)
    }
}

fun Route.getInfo(repo: UserRepository) = get("/user/info") {
    getAuthenticatedUsersId(repo)?.let { userId ->
        repo.findById(userId).getOrThrow().let { user ->
            call.respond(HttpStatusCode.OK, user)
        }
    }
}

fun Route.getName(repo: UserRepository) = get("/user/name") {
    getAuthenticatedUsersId(repo)?.let { userId ->
        repo.findById(userId).getOrThrow().let { user ->
            call.respond(HttpStatusCode.OK, mapOf("name" to user.name))
        }
    }
}

fun Route.getByEmail(repo: UserRepository) = get("/user/by-email") {
    call.receive<String>().let { email ->
        repo.findByEmail(email).getOrThrow().let { user ->
            call.respond(HttpStatusCode.OK, user)
        }
    }
}

fun Route.getById(repo: UserRepository) = get("/user/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.findById(id).getOrThrow().let { user ->
            call.respond(HttpStatusCode.OK, user)
        }
    } ?: throw MissingRequestParameterException("ID")
}

fun Route.edit(repo: UserRepository) = put("/user/edit") {
    call.receive<User>().let { user ->
        repo.edit(user).getOrThrow()
        call.respond(HttpStatusCode.OK, "User edited successfully.")
    }
}

fun Route.delete(repo: UserRepository) = delete("/user/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow()
        call.respond(HttpStatusCode.OK, "User deleted successfully.")
    } ?: throw MissingRequestParameterException("ID")
}

fun Route.changePassword(repo: UserRepository) = put("/user/change-password") {
    getAuthenticatedUsersId(repo)?.let { userId ->
        call.receive<ChangePasswordRequest>().let { request ->
            repo.changePassword(userId, request.oldPassword, request.newPassword)
                .onSuccess {
                    call.respond(HttpStatusCode.OK, "Password changed successfully.")
                }.onFailure {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        if (it.message == "Old password is incorrect") it.message!!
                        else "Something went wrong."
                    )
                }
        }
    }
}