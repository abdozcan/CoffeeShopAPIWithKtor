package com.example.routes

import com.example.data.utils.Constant.ADDRESS_LENGTH
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.userRoutes(userRepo: UserRepository) = routing {
    authenticate {
        getByEmail(userRepo)
        setDefaultAddress(userRepo)
        delete(userRepo)
    }
}

fun Route.getByEmail(repo: UserRepository) = get("/users/{email?}") {
    call.parameters["email"]?.let { email ->
        repo.findByEmail(email).getOrThrow().let { user ->
                call.respond(HttpStatusCode.OK, user)
        }
    } ?: throw MissingRequestParameterException("email")
}

fun Route.setDefaultAddress(repo: UserRepository) = post("/user/{userId?}/default-address") {
    call.parameters["userId"]?.toInt()?.let { userId ->
        call.receiveText().let { address ->
            if (address.length > ADDRESS_LENGTH)
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Address length must be less than $ADDRESS_LENGTH."
                )

            repo.selectDefaultAddress(userId, address).getOrThrow().let { user ->
                call.respond(HttpStatusCode.OK, user)
            }
        }
    } ?: throw MissingRequestParameterException("user ID")
}

fun Route.delete(repo: UserRepository) = delete("/users/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow()
        call.respond(HttpStatusCode.OK, "User deleted successfully.")
    } ?: throw MissingRequestParameterException("ID")
}

@Serializable
data class AddressRequest(
    val name: String,
    val address: String
)