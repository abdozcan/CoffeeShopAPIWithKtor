package com.example.routes

import com.example.domain.model.Review
import com.example.domain.repository.ReviewRepository
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.reviewRoutes(reviewRepo: ReviewRepository) = routing {
    getAllByProductId(reviewRepo)
    getAllByUserId(reviewRepo)
    add(reviewRepo)
    edit(reviewRepo)
    delete(reviewRepo)
}

fun Route.getAllByProductId(repo: ReviewRepository) = get("review/product/{productId?}") {
    call.parameters["productId"]?.toInt()?.let { id ->
        repo.findAllByProductId(id).getOrThrow().let { reviewList ->
            call.respond(reviewList)
        }
    } ?: throw MissingRequestParameterException("product ID")
}

fun Route.getAllByUserId(repo: ReviewRepository) = get("review/user/{userId?}") {
    call.parameters["userId"]?.toInt()?.let { id ->
        repo.findAllByUserId(id).getOrThrow().let { reviewList ->
            call.respond(reviewList)
        }
    } ?: throw MissingRequestParameterException("user ID")
}

fun Route.add(repo: ReviewRepository) = post("review/add") {
    call.receive<Review>().let { review ->
        repo.add(review).getOrThrow().let {
            call.respond("Review added successfully.")
        }
    }
}

fun Route.edit(repo: ReviewRepository) = put("review/edit") {
    call.receive<Review>().let { review ->
        repo.edit(review).getOrThrow().let {
            call.respond("Review edited successfully.")
        }
    }
}

fun Route.delete(repo: ReviewRepository) = delete("review/delete/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow()
        call.respond("Review deleted successfully.")
    } ?: throw MissingRequestParameterException("ID")
}