package com.example.routes

import com.example.domain.model.Review
import com.example.domain.repository.ReviewRepository
import com.example.routes.utils.by
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.reviewRoutes(reviewRepo: ReviewRepository) = routing {
    route("/review") {
        getAllByProductId(reviewRepo)
        getAllByUserId(reviewRepo)
        add(reviewRepo)
        edit(reviewRepo)
        delete(reviewRepo)
    }
}

fun Route.getAllByProductId(repo: ReviewRepository) = route("/product") {
    by { id, limit, offset ->
        repo.findAllByProductId(id, limit, offset).getOrThrow().let { reviewList ->
            call.respond(reviewList)
        }
    }
}

fun Route.getAllByUserId(repo: ReviewRepository) = route("/user") {
    by { id, limit, offset ->
        repo.findAllByUserId(id, limit, offset).getOrThrow().let { reviewList ->
            call.respond(reviewList)
        }
    }
}

fun Route.add(repo: ReviewRepository) = post("/add") {
    call.receive<Review>().let { review ->
        repo.add(review).getOrThrow().let {
            call.respond("Review added successfully.")
        }
    }
}

fun Route.edit(repo: ReviewRepository) = put("/edit") {
    call.receive<Review>().let { review ->
        repo.edit(review).getOrThrow().let {
            call.respond("Review edited successfully.")
        }
    }
}

fun Route.delete(repo: ReviewRepository) = delete("/delete/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow()
        call.respond("Review deleted successfully.")
    } ?: throw MissingRequestParameterException("ID")
}
