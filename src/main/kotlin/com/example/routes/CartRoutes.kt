package com.example.routes

import com.example.domain.model.CartDeleteRequest
import com.example.domain.model.CartItemRequest
import com.example.domain.repository.CartRepository
import com.example.domain.repository.UserRepository
import com.example.routes.utils.getAuthenticatedUsersId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.cartRoutes(cartRepo: CartRepository, userRepo: UserRepository) = routing {
    authenticate {
        getAll(cartRepo, userRepo)
        add(cartRepo, userRepo)
        delete(cartRepo)
    }
}

private fun Route.getAll(repo: CartRepository, userRepo: UserRepository) = get("/cart/all") {
    getAuthenticatedUsersId(userRepo = userRepo)?.let { userId ->
        repo.findAllByUserId(userId).getOrThrow().let { cartList ->
            call.respond(cartList)
        }
    }
}

private fun Route.add(cartRepo: CartRepository, userRepo: UserRepository) = post("/cart/add") {
    getAuthenticatedUsersId(userRepo)?.let { userId ->
        call.receive<CartItemRequest>().let { cartItem ->
            cartRepo.add(userId, cartItem.productId, cartItem.quantity).getOrThrow()
            call.respond(HttpStatusCode.OK, true)
        }
    }
}

private fun Route.delete(repo: CartRepository) = post("/cart/delete") {
    call.receive<CartDeleteRequest>().let { request ->
        if (request.ids.isEmpty())
            return@post call.respond(HttpStatusCode.BadRequest, "No ID found.")

        repo.delete(request.ids).getOrThrow()
        call.respond(
            HttpStatusCode.OK, if (request.ids.size == 1) {
                "Cart"
            } else {
                "Carts"
            } + " deleted successfully."
        )
    }
}