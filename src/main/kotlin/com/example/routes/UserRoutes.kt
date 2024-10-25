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

fun Application.userRoutes(userRepo: UserRepository) = routing {
    authenticate {
        getByEmail(userRepo)
        getById(userRepo)
        setDefaultAddress(userRepo)
        delete(userRepo)
    }
}

fun Route.getByEmail(repo: UserRepository) = get("/user/by-email") {
    call.receive<String>().let { email ->
        repo.findByEmail(email).getOrThrow().let { user ->
                call.respond(HttpStatusCode.OK, user)
        }
    }
}

fun Route.getById(repo: UserRepository) = get("/user/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.findById(id).getOrThrow().let { user ->
            call.respond(HttpStatusCode.OK, user)
        }
    } ?: throw MissingRequestParameterException("ID")
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

fun Route.delete(repo: UserRepository) = delete("/user/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow()
        call.respond(HttpStatusCode.OK, "User deleted successfully.")
    } ?: throw MissingRequestParameterException("ID")
}