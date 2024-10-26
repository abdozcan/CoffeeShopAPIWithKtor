package com.example.routes.utils

import io.ktor.server.plugins.*
import io.ktor.server.routing.*

fun Route.by(block: suspend RoutingContext.(id: Int, limit: Int, offset: Long) -> Unit) =
    route("/{id?}") {
        by { limit, offset ->
            call.parameters["id"]?.toInt()?.let { id ->
                block(id, limit, offset)
            } ?: throw MissingRequestParameterException("ID")
        }
    }

fun Route.by(block: suspend RoutingContext.(limit: Int, offset: Long) -> Unit) =
    get("/limit/{limit?}/page/{page?}") {
        call.parameters["limit"]?.toInt()?.let { limit ->
            call.parameters["page"]?.toLong()?.let { page ->
                val offset = (page - 1) * limit
                block(limit, offset)
            } ?: throw MissingRequestParameterException("page")
        } ?: throw MissingRequestParameterException("limit")
    }