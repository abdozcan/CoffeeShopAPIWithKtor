package com.example.routes

import com.example.domain.model.EditReviewRequest
import com.example.domain.model.ReviewRequest
import com.example.domain.repository.ReviewRepository
import com.example.domain.repository.UserRepository
import com.example.domain.utils.ReviewSortOption
import com.example.routes.utils.getAuthenticatedUsersId
import com.example.routes.utils.getBy
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.reviewRoutes(reviewRepo: ReviewRepository, userRepo: UserRepository) = routing {
    route("/review") {
        getAllByProductId(reviewRepo)
        authenticate {
            getAllByOrderId(reviewRepo, userRepo)
            getAllByUserId(reviewRepo, userRepo)
            add(reviewRepo, userRepo)
            edit(reviewRepo)
            delete(reviewRepo)
        }
    }
}

fun Route.getAllByProductId(reviewRepo: ReviewRepository) = route("/product") {
    getBy<ReviewSortOption> { id, limit, offset, sortOption ->
        reviewRepo.findAllByProductId(id, limit, offset, sortOption).getOrThrow().let { reviewList ->
            call.respond(reviewList)
        }
    }
}

fun Route.getAllByOrderId(reviewRepo: ReviewRepository, userRepo: UserRepository) = get("/order/{orderId?}") {
    getAuthenticatedUsersId(userRepo)?.let { userId ->
        call.parameters["orderId"]?.toInt()?.let { orderId ->
            reviewRepo.findAllByOrderId(orderId, userId).getOrThrow().let { review ->
                call.respond(review)
            }
        } ?: throw MissingRequestParameterException("Order ID")
    }
}

fun Route.getAllByUserId(reviewRepo: ReviewRepository, userRepo: UserRepository) = route("/user") {
    getBy<ReviewSortOption> { limit, offset, sortOption ->
        getAuthenticatedUsersId(userRepo)?.let { userId ->
            reviewRepo.findAllByUserId(userId, limit, offset, sortOption).getOrThrow().let { reviewList ->
                call.respond(reviewList)
            }
        }
    }
}

fun Route.add(reviewRepo: ReviewRepository, userRepo: UserRepository) = post("/add") {
    getAuthenticatedUsersId(userRepo)?.let { userId ->
        call.receive<ReviewRequest>().let { reviewRequest ->
            reviewRepo.add(reviewRequest, userId).getOrThrow().let { review ->
                call.respond(review)
            }
        }
    }
}

fun Route.edit(repo: ReviewRepository) = put("/edit") {
    call.receive<EditReviewRequest>().let { review ->
        repo.edit(review).getOrThrow().let {
            call.respond(HttpStatusCode.OK, "Review edited successfully.")
        }
    }
}

fun Route.delete(repo: ReviewRepository) = delete("/delete/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow()
        call.respond("Review deleted successfully.")
    } ?: throw MissingRequestParameterException("ID")
}