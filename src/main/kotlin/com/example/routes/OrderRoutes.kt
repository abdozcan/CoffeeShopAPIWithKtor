package com.example.routes

import com.example.domain.repository.OrderRepository
import com.example.domain.repository.RequestOrderedProduct
import com.example.domain.utils.LocalDateTimeSerializer
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

fun Application.orderRoutes(orderRepo: OrderRepository) = routing {
    authentication {
        findAllByUserId(orderRepo)
        findById(orderRepo)
        findOrderItemProduct(orderRepo)
        cancel(orderRepo)
        delete(orderRepo)
        add(orderRepo)
    }
}

private fun Route.findAllByUserId(repo: OrderRepository) = get("order/user/{userId?}") {
    call.parameters["userId"]?.toInt()?.let { userId ->
        repo.findAllByUserId(userId).getOrThrow().let { orderList ->
            call.respond(orderList)
        }
    } ?: throw MissingRequestParameterException("user ID")
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

private fun Route.add(repo: OrderRepository) = post("order/add") {
    call.receive<OrderRequest>().let { order ->
        repo.add(
            order.userId,
            order.shippingAddress,
            order.paymentMethod,
            order.orderDate,
            order.orderedProducts
        ).getOrThrow().let {
            call.respond("Order added successfully.")
        }
    }
}

@Serializable
data class OrderRequest(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("shipping_address")
    val shippingAddress: String,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("order_date")
    @Serializable(LocalDateTimeSerializer::class)
    val orderDate: LocalDateTime,
    @SerialName("ordered_products")
    val orderedProducts: List<RequestOrderedProduct>
)