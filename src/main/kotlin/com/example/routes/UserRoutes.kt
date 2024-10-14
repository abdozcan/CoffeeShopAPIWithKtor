package com.example.routes

import com.example.domain.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.userRoutes(repo: UserRepository) = routing {
    byEmail(repo)
    address(repo)
}

fun Route.byEmail(repo: UserRepository) = get("/users/{email}") { }
fun Route.address(repo: UserRepository) = get("/users/{userId}/{addressId}") { }