package com.example.routes

import com.example.data.utils.Constant.ADDRESS_LENGTH
import com.example.data.utils.Constant.ADDRESS_NAME_LENGTH
import com.example.domain.model.AddAddressRequest
import com.example.domain.model.Address
import com.example.domain.repository.AddressRepository
import com.example.domain.repository.UserRepository
import com.example.routes.utils.getAuthenticatedUsersId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.addressRoutes(
    addressRepo: AddressRepository,
    userRepo: UserRepository
) = routing {
    authenticate {
        getAll(addressRepo, userRepo)
        getDefault(addressRepo, userRepo)
        setDefaultAddress(addressRepo)
        delete(addressRepo)
        add(addressRepo, userRepo)
        edit(addressRepo)
    }
}

private fun Route.getAll(
    addressRepo: AddressRepository,
    userRepo: UserRepository
) = get("/address/all") {
    getAuthenticatedUsersId(userRepo = userRepo)?.let { userId ->
        addressRepo.findAllByUserId(userId).getOrThrow().let { addressList ->
            call.respond(addressList)
        }
    }
}

private fun Route.getDefault(
    addressRepo: AddressRepository,
    userRepo: UserRepository
) = get("/address/default") {
    getAuthenticatedUsersId(userRepo)?.let { userId ->
        addressRepo.findDefaultByUserId(userId).getOrThrow().let { address ->
            call.respond(address)
        }
    }
}

private fun Route.setDefaultAddress(
    addressRepo: AddressRepository
) = post("/address/set-default/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        addressRepo.setDefaultAddress(id).getOrThrow().let {
            call.respond("Default address set successfully.")
        }
    } ?: throw MissingRequestParameterException("ID")
}

private fun Route.add(addressRepo: AddressRepository, userRepo: UserRepository) = post("/address/add") {
    getAuthenticatedUsersId(userRepo)?.let { userId ->
        call.receive<AddAddressRequest>().let { address ->
            if (address.name.length > ADDRESS_NAME_LENGTH)
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Address name length must be less than $ADDRESS_NAME_LENGTH."
                )

            if (address.address.length > ADDRESS_LENGTH)
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Address length must be less than $ADDRESS_LENGTH."
                )

            addressRepo.add(address.name, userId, address.address).getOrThrow().let { address ->
                call.respond(HttpStatusCode.OK, address)
            }
        }
    }
}

private fun Route.delete(repo: AddressRepository) = post("/address/delete/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow().let {
            call.respond("Address deleted successfully.")
        }
    } ?: throw MissingRequestParameterException("ID")
}

private fun Route.edit(repo: AddressRepository) = put("/address/edit") {
    call.receive<Address>().let { address ->
        repo.edit(address).getOrThrow().let {
            call.respond("Address edited successfully.")
        }
    }
}