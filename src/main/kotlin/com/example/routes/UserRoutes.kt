package com.example.routes

import com.example.data.utils.Constant.ADDRESS_LENGTH
import com.example.data.utils.Constant.ADDRESS_NAME_LENGTH
import com.example.data.utils.doOrThrowIfNull
import com.example.domain.repository.AddressRepository
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.userRoutes(userRepo: UserRepository, addressRepo: AddressRepository) = routing {
    authenticate {
        getByEmail(userRepo)
        getAddresses(addressRepo)
        setDefaultAddress(userRepo)
        addAddress(addressRepo)
        delete(userRepo)
    }
}

fun Route.getByEmail(repo: UserRepository) = get("/users/{email}") {
    call.parameters["email"]?.let { email ->
        repo.findByEmail(email)
            .onSuccess { user ->
                call.respond(HttpStatusCode.OK, user)
            }.onFailure { exception ->
                when (exception) {
                    is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User not found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    } ?: call.respond(HttpStatusCode.BadRequest, "Email parameter is missing.")
}

fun Route.getAddresses(addressRepo: AddressRepository) = get("/users/{userId}/addresses") {
    runCatching {
        call.parameters["userID"].doOrThrowIfNull { userId -> userId.toInt() }
    }.onSuccess { userId ->
        addressRepo.findAllByUserId(userId)
            .onSuccess { addressList ->
                call.respond(HttpStatusCode.OK, addressList)
            }.onFailure { exception ->
                when (exception) {
                    is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "No addresses found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    }.onFailure { exception ->
        when (exception) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid User ID.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User ID parameter is missing.")
        }
    }
}

fun Route.setDefaultAddress(repo: UserRepository) = post("/users/{userId}/default_address/set") {
    runCatching {
        call.parameters["userId"].doOrThrowIfNull { userId -> userId.toInt() }
    }.onSuccess { userId ->
        runCatching {
            call.receiveText().doOrThrowIfNull { address ->
                if (address.length > ADDRESS_LENGTH)
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        "Address length must be less than $ADDRESS_LENGTH."
                    )

                address
            }
        }.onSuccess { address ->
            repo.selectDefaultAddress(userId, address)
                .onSuccess { user ->
                    call.respond(HttpStatusCode.OK, user)
                }.onFailure { exception ->
                    when (exception) {
                        is NotFoundException -> call.respond(HttpStatusCode.NotFound, "Invalid User ID.")
                        else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                    }
                }
        }.onFailure {
            call.respond(HttpStatusCode.BadRequest, "Invalid request body.")
        }
    }.onFailure { exception ->
        when (exception) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid User ID.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User ID parameter is missing.")
        }
    }
}

fun Route.addAddress(addressRepo: AddressRepository) = post("/users/{userId}/address/add") {
    runCatching {
        call.parameters["userId"].doOrThrowIfNull { userId -> userId.toInt() }
    }.onSuccess { userId ->
        runCatching {
            call.receive<AddressRequest>().doOrThrowIfNull { addressRequest ->
                if (addressRequest.name.length > ADDRESS_NAME_LENGTH)
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        "Address name length must be less than $ADDRESS_NAME_LENGTH."
                    )

                if (addressRequest.address.length > ADDRESS_LENGTH)
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        "Address length must be less than $ADDRESS_LENGTH."
                    )

                addressRequest
            }
        }.onSuccess { addressRequest ->
            addressRepo.add(addressRequest.name, userId, addressRequest.address)
                .onSuccess { address ->
                    call.respond(HttpStatusCode.OK, address)
                }.onFailure {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
        }.onFailure {
            call.respond(HttpStatusCode.BadRequest, "Invalid request body.")
        }
    }.onFailure { exception ->
        when (exception) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid User ID.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User ID parameter is missing.")
        }
    }
}

fun Route.delete(repo: UserRepository) = delete("/users/{id}") {
    runCatching {
        call.parameters["id"].doOrThrowIfNull { id -> id.toInt() }
    }.onSuccess {
        repo.delete(it)
            .onSuccess {
                call.respond(HttpStatusCode.OK, "User deleted successfully.")
            }.onFailure { exception ->
                when (exception) {
                    is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User not found.")
                    else -> call.respond(HttpStatusCode.InternalServerError, "Internal Server Error.")
                }
            }
    }.onFailure { exception ->
        when (exception) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid User ID.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, "User ID parameter is missing.")
        }
    }
}

@Serializable
private data class AddressRequest(
    val name: String,
    val address: String
)