package com.example.routes

import com.example.domain.model.CartItemRequest
import com.example.domain.repository.CartRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.cartRoutes(cartRepo: CartRepository) = routing {
    authenticate {
        getAll(cartRepo)
        add(cartRepo)
        delete(cartRepo)
    }
}

private fun Route.getAll(repo: CartRepository) = get("/cart/user/{userId?}") {
    call.parameters["userId"]?.toInt()?.let { userId ->
        repo.findAllByUserId(userId).getOrThrow().let { cartList ->
            call.respond(cartList)
        }
    } ?: throw MissingRequestParameterException("user ID")
}

private fun Route.add(repo: CartRepository) = post("/cart/add") {
    call.receive<CartItemRequest>().let { cartItem ->
        repo.add(cartItem.userId, cartItem.productId, cartItem.quantity).getOrThrow()
        call.respond(HttpStatusCode.OK, "Added to cart.")
    }
}

private fun Route.delete(repo: CartRepository) = delete("/cart/delete") {
    call.receive<List<Int>>().let { ids ->
        if (ids.isEmpty())
            return@delete call.respond(HttpStatusCode.BadRequest, "No ID found.")

        repo.delete(ids).getOrThrow()
        call.respond(
            HttpStatusCode.OK, if (ids.size == 1) {
                "Cart"
            } else {
                "Carts"
            } + " deleted successfully."
        )
    }
}