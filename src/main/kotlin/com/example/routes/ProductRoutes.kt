package com.example.routes

import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
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
    repo.all().getOrThrow().let { productList ->
            call.respond(productList)
        }
}

fun Route.getById(repo: ProductRepository) = get("/products/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.findById(id).getOrThrow().let { product ->
                call.respond(product)
            }
    } ?: throw MissingRequestParameterException("ID")
}

fun Route.getByCategory(repo: ProductRepository) = get("/products/category/{category?}") {
    call.parameters["category"]?.let { category: String ->
        repo.findByCategory(category).getOrThrow().let { products: List<Product> ->
            call.respond(products)
        }
    } ?: throw MissingRequestParameterException("category name")
}

fun Route.getBestseller(repo: ProductRepository) = get("/products/bestseller") {
    repo.findBestsellers().getOrThrow().let { productList ->
            call.respond(productList)
        }
}

fun Route.getFavoriteByUserId(repo: ProductRepository) = authenticate {
    get("/products/favorite/{userId?}") {
        call.parameters["userId"]?.toInt()?.let { userId ->
            repo.findFavoritedProduct(userId).getOrThrow().let { productList ->
                    call.respond(productList)
                }
        } ?: throw MissingRequestParameterException("user ID")
    }
}
