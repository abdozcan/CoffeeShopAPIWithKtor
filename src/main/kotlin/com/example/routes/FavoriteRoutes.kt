package com.example.routes

import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.domain.repository.FavoriteRepository
import com.example.domain.repository.UserRepository
import com.example.domain.utils.ProductSortOption
import com.example.routes.utils.getAuthenticatedUsersId
import com.example.routes.utils.getBy
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Tag(["Favorite"])
fun Application.favoriteRoutes(favoriteRepo: FavoriteRepository, userRepo: UserRepository) = routing {
    authenticate {
        getAll(favoriteRepo, userRepo)
        isFavorite(favoriteRepo, userRepo)
        add(favoriteRepo, userRepo)
        delete(favoriteRepo, userRepo)
    }
}

private fun Route.getAll(
    favoriteRepo: FavoriteRepository,
    userRepo: UserRepository
) = route("/favorite/all") {
    getBy<ProductSortOption> { limit, offset, sortOption ->
        getAuthenticatedUsersId(userRepo)?.let { userId ->
            favoriteRepo.findAllByUserId(userId, limit, offset, sortOption).getOrThrow().let { favoriteList ->
                call.respond(favoriteList)
            }
        } ?: throw JWTVerificationException("Unauthorized")
    }
}

private fun Route.isFavorite(favoriteRepo: FavoriteRepository, userRepo: UserRepository) =
    post("/favorite/is-favorite/{product_id?}") {
        getAuthenticatedUsersId(userRepo)?.let { userId ->
            call.parameters["product_id"]?.toInt()?.let { productId ->
                favoriteRepo.isFavorite(userId, productId).getOrThrow().let { isFavorite ->
                    call.respond(HttpStatusCode.OK, isFavorite)
                }
            } ?: throw MissingRequestParameterException("product ID")
        } ?: throw JWTVerificationException("Unauthorized")
}

private fun Route.add(favoriteRepo: FavoriteRepository, userRepo: UserRepository) =
    post("/favorite/add/{product_id?}") {
        getAuthenticatedUsersId(userRepo)?.let { userId ->
            call.parameters["product_id"]?.toInt()?.let { productId ->
                favoriteRepo.add(userId, productId).getOrThrow()
                call.respond(HttpStatusCode.OK, "Added to favorite.")
            } ?: throw MissingRequestParameterException("product ID")
        } ?: throw JWTVerificationException("Unauthorized")
}

private fun Route.delete(favoriteRepo: FavoriteRepository, userRepo: UserRepository) =
    delete("/favorite/delete/{product_id?}") {
        getAuthenticatedUsersId(userRepo)?.let { userId ->
            call.parameters["product_id"]?.toInt()?.let { productId ->
                favoriteRepo.delete(userId, productId).getOrThrow()
                call.respond(HttpStatusCode.OK, "Favorite deleted successfully.")
            } ?: throw MissingRequestParameterException("product ID")
        } ?: throw JWTVerificationException("Unauthorized")
}