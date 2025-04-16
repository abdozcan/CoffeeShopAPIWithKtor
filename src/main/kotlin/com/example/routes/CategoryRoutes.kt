package com.example.routes

import com.example.domain.repository.CategoryRepository
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Tag(["Category"])
fun Application.categoryRoutes(repo: CategoryRepository) {
    routing {
        getAll(repo)
    }
}

private fun Route.getAll(repo: CategoryRepository) = get("/category") {
    repo.findAll().getOrThrow().let { categories ->
        call.respond(HttpStatusCode.OK, categories)
    }
}