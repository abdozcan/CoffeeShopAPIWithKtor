package com.example.routes.utils

import io.ktor.server.plugins.*
import io.ktor.server.routing.*

fun Route.by(block: suspend RoutingContext.(id: Int, limit: Int, offset: Long) -> Unit) =
    get("/{id?}") {
        call.parameters["id"]?.toInt()?.let { id ->
            by { limit, offset ->
                block(id, limit, offset)
            }
        } ?: throw MissingRequestParameterException("ID")
    }

fun Route.by(block: suspend Route.(limit: Int, offset: Long) -> Unit) =
    get("/limit/{limit?}/page/{page?}") {
        call.parameters["limit"]?.toInt()?.let { limit ->
            call.parameters["page"]?.toLong()?.let { page ->
                val offset = (page - 1) * limit
                block(limit, offset)
            } ?: throw MissingRequestParameterException("page")
        } ?: throw MissingRequestParameterException("limit")
    }