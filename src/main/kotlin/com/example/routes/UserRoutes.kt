package com.example.routes

import com.example.domain.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.userRoutes(repo: UserRepository) = routing {
    getByEmail(repo)
    getAddress(repo)
}

fun Route.getByEmail(repo: UserRepository) = get("/users/{email}") { }
fun Route.getAddress(repo: UserRepository) = get("/users/{userId}/addresses/{addressId}") { }