package com.example.routes

import com.example.domain.repository.ProductRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.productRoutes(repo: ProductRepository) = routing {
    all(repo)
    byId(repo)
    byCategoryId(repo)
    bestseller(repo)
    favoriteByUserId(repo)
}

fun Route.all(repo: ProductRepository) = get("/products") {

}

fun Route.byId(repo: ProductRepository) = get("/products/{id}") {

}

fun Route.byCategoryId(repo: ProductRepository) = get("/products/{categoryId}") {

}

fun Route.bestseller(repo: ProductRepository) = get("/products/bestseller") {

}

fun Route.favoriteByUserId(repo: ProductRepository) = get("/products/favorite/{userId}") {

}