package com.example.routes

import com.example.data.utils.doOrThrowIfNull
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
        getAddresses(addressRepo)
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

fun Route.getAddresses(addressRepo: AddressRepository) = get("/users/{userId}/addresses") {
    runCatching {
        call.parameters["userID"].doOrThrowIfNull { userId -> userId.toInt() }
    }.onSuccess { userId ->
        addressRepo.findAllByUserId(userId)
            .onSuccess { addressList ->
                call.respond(HttpStatusCode.OK, addressList)
            }.onFailure { exception ->
                when (exception) {
                    is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "No addresses found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    }.onFailure { exception ->
        when (exception) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid User ID.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User ID parameter is missing.")
        }
    }
}