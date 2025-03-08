package com.example.routes

import com.example.domain.model.CartDeleteRequest
import com.example.domain.model.CartItemRequest
import com.example.domain.model.PromoRequest
import com.example.domain.repository.CartRepository
import com.example.domain.repository.PromoCodeRepository
import com.example.domain.repository.UserRepository
import com.example.routes.utils.getAuthenticatedUsersId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.cartRoutes(
    cartRepo: CartRepository,
    userRepo: UserRepository,
    promoCodeRepo: PromoCodeRepository
) = routing {
    authenticate {
        getItemsSize(cartRepo, userRepo)
        getAll(cartRepo, userRepo)
        add(cartRepo, userRepo)
        delete(cartRepo)
        applyPromoCode(promoCodeRepo, userRepo)
    }
}

private fun Route.getItemsSize(repo: CartRepository, userRepo: UserRepository) = get("/cart/items_size") {
    getAuthenticatedUsersId(userRepo = userRepo)?.let { userId ->
        repo.findAllByUserId(userId).getOrThrow().size.let { size ->
            call.respond(HttpStatusCode.OK, size)
        }
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

private fun Route.applyPromoCode(
    promoCodeRepo: PromoCodeRepository,
    userRepo: UserRepository
) = post("cart/apply-promo-code") {
    getAuthenticatedUsersId(userRepo)?.let { userId ->
        call.receive<PromoRequest>().let { request ->
            promoCodeRepo.validate(
                userId = userId,
                code = request.code,
                totalPrice = request.totalPrice
            ).getOrThrow().let { respond ->
                call.respond(HttpStatusCode.OK, respond)
            }
        }
    }
}