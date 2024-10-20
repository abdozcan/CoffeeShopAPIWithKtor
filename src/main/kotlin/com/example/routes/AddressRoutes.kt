package com.example.routes

import com.example.data.utils.Constant.ADDRESS_LENGTH
import com.example.data.utils.Constant.ADDRESS_NAME_LENGTH
import com.example.domain.model.Address
import com.example.domain.repository.AddressRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Application.addressRoutes(addressRepo: AddressRepository) = routing {
    authentication {
        findAllByUserId(addressRepo)
        delete(addressRepo)
        add(addressRepo)
        edit(addressRepo)
    }
}

private fun Route.findAllByUserId(repo: AddressRepository) = get("address/user/{userId?}") {
    call.parameters["userId"]?.toInt()?.let { userId ->
        repo.findAllByUserId(userId).getOrThrow().let { addressList ->
            call.respond(addressList)
        }
    } ?: throw MissingRequestParameterException("user ID")
}

private fun Route.add(repo: AddressRepository) = post("address/add") {
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

        repo.add(address.name, address.userId, address.address).getOrThrow().let {
            call.respond("Address added successfully.")
        }
    }
}

private fun Route.delete(repo: AddressRepository) = delete("address/delete/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow().let {
            call.respond("Address deleted successfully.")
        }
    } ?: throw MissingRequestParameterException("ID")
}

private fun Route.edit(repo: AddressRepository) = put("address/edit") {
    call.receive<Address>().let { address ->
        repo.edit(address).getOrThrow().let {
            call.respond("Address edited successfully.")
        }
    }
}

@Serializable
data class AddAddressRequest(
    val name: String,
    @SerialName("user_id")
    val userId: Int,
    val address: String
)