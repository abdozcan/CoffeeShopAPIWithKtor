package com.example.routes

import com.example.data.utils.doOrThrowIfNull
import com.example.domain.repository.ProductRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.productRoutes(repo: ProductRepository) = routing {
    getAll(repo)
    getById(repo)
    getByCategory(repo)
    getBestseller(repo)
    getFavoriteByUserId(repo)
}

fun Route.getAll(repo: ProductRepository) = get("/products") {
    repo.all()
        .onSuccess { productList ->
            call.respond(productList)
        }.onFailure { exception ->
            when (exception) {
                is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "No products found.")
                else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
            }
        }
}

fun Route.getById(repo: ProductRepository) = get("/products/{id}") {
    runCatching {
        call.parameters["id"].doOrThrowIfNull { id -> id.toInt() }
    }.onSuccess { id ->
        repo.findById(id)
            .onSuccess { product ->
                call.respond(product)
            }.onFailure { exception ->
                when (exception) {
                    is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "No product found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    }.onFailure { exception ->
        when (exception) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid ID.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, "ID parameter is missing.")
        }
    }
}

fun Route.getByCategory(repo: ProductRepository) = get("/products/{category}") {
    call.parameters["category"]?.let { category: String ->
        repo.findByCategory(category)
            .onSuccess { productList ->
                call.respond(productList)
            }.onFailure { exception ->
                when (exception) {
                    is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "No products found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    } ?: call.respond(HttpStatusCode.BadRequest, "Category parameter is missing.")
}

fun Route.getBestseller(repo: ProductRepository) = get("/products/bestseller") {
    repo.findBestsellers()
        .onSuccess { productList ->
            call.respond(productList)
        }.onFailure { exception ->
            when (exception) {
                is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "No bestsellers found.")
                else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
            }
        }
}

fun Route.getFavoriteByUserId(repo: ProductRepository) = get("/products/favorite/{userId}") {
    runCatching {
        call.parameters["userId"].doOrThrowIfNull { userId -> userId.toInt() }
    }.onSuccess { userId ->
        repo.findFavoritedProduct(userId)
            .onSuccess { productList ->
                call.respond(productList)
            }.onFailure { exception ->
                when (exception) {
                    is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "No products found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    }.onFailure { exception ->
        when (exception) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid ID.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User ID parameter is missing.")
        }
    }
}