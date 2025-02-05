package com.example.routes

import com.example.domain.model.Review
import com.example.domain.repository.ReviewRepository
import com.example.domain.utils.ReviewSortOption
import com.example.routes.utils.getBy
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.reviewRoutes(reviewRepo: ReviewRepository) = routing {
    route("/review") {
        getAllByProductId(reviewRepo)
        getAll(reviewRepo)
        add(reviewRepo)
        edit(reviewRepo)
        delete(reviewRepo)
    }
}

fun Route.getAllByProductId(repo: ReviewRepository) = route("/product") {
    getBy<ReviewSortOption> { id, limit, offset, sortOption ->
        repo.findAllByProductId(id, limit, offset, sortOption).getOrThrow().let { reviewList ->
            call.respond(reviewList)
        }
    }
}

fun Route.getAll(repo: ReviewRepository) = route("/user") {
    getBy<ReviewSortOption> { id, limit, offset, sortOption ->
        repo.findAllByUserId(id, limit, offset, sortOption).getOrThrow().let { reviewList ->
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
