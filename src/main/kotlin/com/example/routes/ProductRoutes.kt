package com.example.routes

import com.example.domain.model.Product
import com.example.domain.model.SearchRequest
import com.example.domain.repository.ProductRepository
import com.example.routes.utils.by
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.productRoutes(repo: ProductRepository) = routing {
    route("/product") {
        getAll(repo)
        getById(repo)
        getByCategory(repo)
        getBestseller(repo)
        getFavoriteByUserId(repo)
        search(repo)
    }
}

fun Route.getAll(repo: ProductRepository) = get {
    by { limit, offset ->
        repo.all(limit, offset).getOrThrow().let { productList ->
            call.respond(productList)
        }
    }
}

fun Route.getById(repo: ProductRepository) {
    by { id, limit, offset ->
        repo.findById(id, limit, offset).getOrThrow().let { product ->
            call.respond(product)
        }
    }
}

fun Route.getByCategory(repo: ProductRepository) = get("/category/{category?}") {
    call.parameters["category"]?.let { category: String ->
        by { limit, offset ->
            repo.findByCategory(category, limit, offset).getOrThrow().let { products: List<Product> ->
                call.respond(products)
            }
        }
    } ?: throw MissingRequestParameterException("category name")
}

fun Route.getBestseller(repo: ProductRepository) = get("/bestseller") {
    by { limit, offset ->
        repo.findBestsellers(limit, offset).getOrThrow().let { productList ->
            call.respond(productList)
        }
    }
}

fun Route.getFavoriteByUserId(repo: ProductRepository) = authenticate {
    get("/favorite") {
        by { id, limit, offset ->
            repo.findFavoriteProduct(id, limit, offset).getOrThrow().let { productList ->
                call.respond(productList)
            }
        }
    }
}

fun Route.search(repo: ProductRepository) = post("/search") {
    call.receive<SearchRequest>().let { request ->
        repo.search(request).getOrThrow().let { productList ->
            call.respond(productList)
        }
    }
}