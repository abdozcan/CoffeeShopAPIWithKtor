package com.example.routes

import com.example.domain.repository.FavoriteRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Application.favoriteRoutes(favoriteRepo: FavoriteRepository) = routing {
    authenticate {
        getAll(favoriteRepo)
        isFavorite(favoriteRepo)
        add(favoriteRepo)
        delete(favoriteRepo)
    }
}

private fun Route.getAll(repo: FavoriteRepository) = get("/favorite/user/{userId?}") {
    call.parameters["userId"]?.toInt()?.let { userId ->
        repo.findAllByUserId(userId).getOrThrow().let { favoriteList ->
            call.respond(favoriteList)
        }
    } ?: throw MissingRequestParameterException("user ID")
}

private fun Route.isFavorite(repo: FavoriteRepository) = post("/favorite/is-favorite") {
    call.receive<FavoriteItem>().let { item ->
        repo.isFavorite(item.userId, item.productId).getOrThrow().let {
            call.respond(HttpStatusCode.OK, it)
        }
    }
}

private fun Route.add(repo: FavoriteRepository) = post("/favorite/add") {
    call.receive<FavoriteItem>().let { item ->
        repo.add(item.userId, item.productId).getOrThrow()
        call.respond(HttpStatusCode.OK, "Added to cart.")
    }
}

private fun Route.delete(repo: FavoriteRepository) = delete("/favorite/delete/{product_id?}") {
    call.parameters["product_id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow()
        call.respond(HttpStatusCode.OK, "Favorite deleted successfully.")
    } ?: throw MissingRequestParameterException("product ID")
}

@Serializable
data class FavoriteItem(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("product_id")
    val productId: Int
)
