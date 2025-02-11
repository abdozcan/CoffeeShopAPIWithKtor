package com.example.routes

import com.example.domain.model.OrderRequest
import com.example.domain.repository.OrderRepository
import com.example.domain.repository.UserRepository
import com.example.domain.utils.OrderSortOption
import com.example.routes.utils.getAuthenticatedUsersId
import com.example.routes.utils.getBy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.orderRoutes(orderRepo: OrderRepository, userRepo: UserRepository) = routing {
    authenticate {
        getAll(orderRepo, userRepo)
        findById(orderRepo)
        findOrderItemProduct(orderRepo)
        cancel(orderRepo)
        delete(orderRepo)
        add(orderRepo, userRepo)
    }
}

private fun Route.getAll(orderRepo: OrderRepository, userRepo: UserRepository) = route("order/all") {
    getBy<OrderSortOption> { limit, offset, sortOption ->
        getAuthenticatedUsersId(userRepo)?.let { userId ->
            orderRepo.findAllByUserId(userId, limit, offset, sortOption).getOrThrow().let { orderList ->
                call.respond(orderList)
            }
        }
    }
}

private fun Route.findById(repo: OrderRepository) = get("order/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.findById(id).getOrThrow().let { order ->
            call.respond(order)
        }
    } ?: throw MissingRequestParameterException("ID")
}

private fun Route.findOrderItemProduct(orderRepo: OrderRepository) = get("order/{orderId?}/products") {
    call.parameters["orderId"]?.toInt()?.let { orderId ->
        orderRepo.findOrderItemProduct(orderId).getOrThrow().let { productList ->
            call.respond(productList)
        }
    } ?: throw MissingRequestParameterException("order ID")
}

private fun Route.cancel(repo: OrderRepository) = put("order/cancel/{orderId?}") {
    call.parameters["orderId"]?.toInt()?.let { orderId ->
        repo.cancel(orderId).getOrThrow().let {
            call.respond("Order cancelled successfully.")
        }
    } ?: throw MissingRequestParameterException("order ID")
}

private fun Route.delete(repo: OrderRepository) = delete("order/delete/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow().let {
            call.respond("Order deleted successfully.")
        }
    } ?: throw MissingRequestParameterException("ID")
}

private fun Route.add(repo: OrderRepository, userRepo: UserRepository) = post("order/add") {
    getAuthenticatedUsersId(userRepo = userRepo)?.let { userId ->
        call.receive<OrderRequest>().let { order ->
            repo.add(
                userId,
                order.shippingAddress,
                order.paymentMethod,
                order.orderDate,
                order.totalAmount,
                order.promoCodeId,
                order.orderedProducts
            ).getOrThrow().let { order ->
                call.respond(order)
            }
        }
    }
}