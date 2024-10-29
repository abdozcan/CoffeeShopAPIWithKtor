package com.example.routes

import com.example.domain.model.SearchRequest
import com.example.domain.repository.ProductRepository
import com.example.domain.utils.ProductSortOption
import com.example.routes.utils.getBy
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

fun Route.getAll(repo: ProductRepository) = getBy<ProductSortOption> { limit, offset, sortOption ->
    repo.all(limit, offset, sortOption).getOrThrow().let { productList ->
        call.respond(productList)
    }
}

fun Route.getById(repo: ProductRepository) = get("/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.findById(id).getOrThrow().let { product ->
            call.respond(product)
        }
    } ?: throw MissingRequestParameterException("ID")
}


fun Route.getByCategory(repo: ProductRepository) = route("/category/{category?}") {
    getBy<ProductSortOption> { limit, offset, sortOption ->
        call.parameters["category"]?.let { category: String ->
            repo.findByCategory(category, limit, offset, sortOption).getOrThrow().let { products ->
                call.respond(products)
            }
        } ?: throw MissingRequestParameterException("category name")
    }
}

fun Route.getBestseller(repo: ProductRepository) = route("/bestseller") {
    getBy<ProductSortOption> { limit, offset, sortOption ->
        repo.findBestsellers(limit, offset, sortOption).getOrThrow().let { productList ->
            call.respond(productList)
        }
    }
}

fun Route.getFavoriteByUserId(repo: ProductRepository) = authenticate {
    route("/favorite") {
        getBy<ProductSortOption> { id, limit, offset, sortOption ->
            repo.findFavoriteProduct(id, limit, offset, sortOption).getOrThrow().let { productList ->
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