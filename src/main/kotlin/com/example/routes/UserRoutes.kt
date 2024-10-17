package com.example.routes

import com.example.domain.repository.AddressRepository
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRoutes(userRepo: UserRepository, addressRepo: AddressRepository) = routing {
    authenticate {
        getByEmail(userRepo)
    }
}

fun Route.getByEmail(repo: UserRepository) = get("/users/{email}") {
    call.parameters["email"]?.let { email ->
        repo.findByEmail(email)
            .onSuccess { user ->
                call.respond(HttpStatusCode.OK, user)
            }.onFailure { exception ->
                when (exception) {
                    is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User not found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    } ?: call.respond(HttpStatusCode.BadRequest, "Email parameter is missing.")
}


