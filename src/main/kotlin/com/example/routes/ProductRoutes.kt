package com.example.routes

import com.example.domain.model.SearchRequest
import com.example.domain.repository.ProductRepository
import com.example.domain.repository.UserRepository
import com.example.domain.utils.ProductSortOption
import com.example.routes.utils.getBy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.productRoutes(productRepo: ProductRepository, userRepo: UserRepository) = routing {
    route("/product") {
        getAll(productRepo)
        getById(productRepo, userRepo)
        getByCategory(productRepo)
        getBestseller(productRepo)
        getPopular(productRepo)
        getNewest(productRepo)
        getSpecialOffer(productRepo)
        getFavoriteByUserId(productRepo)
        search(productRepo)
    }
}

fun Route.getAll(repo: ProductRepository) = getBy<ProductSortOption> { limit, offset, sortOption ->
    repo.all(limit, offset, sortOption).getOrThrow().let { productList ->
        call.respond(productList)
    }
}

fun Route.getById(productRepo: ProductRepository, userRepo: UserRepository) = authenticate(
    strategy = AuthenticationStrategy.Optional
) {
    get("/{id?}") {
        call.parameters["id"]?.toInt()?.let { id ->
            // if the user is authenticated, get the user id to check whether the product is favorite
            val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
            val userId: Int? = email?.let { email ->
                userRepo.findByEmail(email).getOrThrow().id
            }

            productRepo.findById(id, userId).getOrThrow().let { product ->
                call.respond(product)
            }
        } ?: throw MissingRequestParameterException("ID")
    }
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

fun Route.getPopular(repo: ProductRepository) = get("/popular/limit/{limit?}/page/{page?}") {
    call.parameters["limit"]?.toInt()?.let { limit ->
        call.parameters["page"]?.toLong()?.let { page ->
            val offset = (page - 1) * limit
            repo.findPopulars(limit, offset).getOrThrow().let { productList ->
                call.respond(productList)
            }
        } ?: throw MissingRequestParameterException("page")
    } ?: throw MissingRequestParameterException("limit")
}

fun Route.getNewest(repo: ProductRepository) = get("/newest/limit/{limit?}/page/{page?}") {
    call.parameters["limit"]?.toInt()?.let { limit ->
        call.parameters["page"]?.toLong()?.let { page ->
            val offset = (page - 1) * limit
            repo.findNewest(limit, offset).getOrThrow().let { productList ->
                call.respond(productList)
            }
        } ?: throw MissingRequestParameterException("page")
    } ?: throw MissingRequestParameterException("limit")
}

fun Route.getSpecialOffer(repo: ProductRepository) = get("/special_offer/limit/{limit?}/page/{page?}") {
    call.parameters["limit"]?.toInt()?.let { limit ->
        call.parameters["page"]?.toLong()?.let { page ->
            val offset = (page - 1) * limit
            repo.findSpecialOffers(limit, offset).getOrThrow().let { productList ->
                call.respond(productList)
            }
        } ?: throw MissingRequestParameterException("page")
    } ?: throw MissingRequestParameterException("limit")
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