package com.example.routes.utils

import com.example.domain.repository.UserRepository
import com.example.domain.utils.ProductSortOption
import com.example.domain.utils.ReviewSortOption
import com.example.domain.utils.SortOption
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

inline fun <reified T : SortOption> Route.getBy(
    crossinline block: suspend RoutingContext.(id: Int, limit: Int, offset: Long, sortOption: T) -> Unit
) = route("/{id?}") {
    getBy<T> { limit, offset, sortOption ->
        call.parameters["id"]?.toInt()?.let { id ->
            block(id, limit, offset, sortOption)
        } ?: throw MissingRequestParameterException("ID")
    }
}

inline fun <reified T : SortOption> Route.getBy(
    crossinline block: suspend RoutingContext.(limit: Int, offset: Long, sortOption: T) -> Unit
) = get("/limit/{limit?}/page/{page?}") {
    call.parameters["limit"]?.toInt()?.let { limit ->
        call.parameters["page"]?.toLong()?.let { page ->
            call.request.queryParameters["sort"].let { sort ->
                val offset = (page - 1) * limit

                val sortOption = when (T::class) {
                    ProductSortOption::class -> ProductSortOption.valueOf(sort ?: ProductSortOption.DATE_DESC.name) as T
                    ReviewSortOption::class -> ReviewSortOption.valueOf(sort ?: ReviewSortOption.DATE_DESC.name) as T
                    else -> throw IllegalArgumentException("Unsupported SortOption type")
                }

                block(limit, offset, sortOption)
            }
        } ?: throw MissingRequestParameterException("page")
    } ?: throw MissingRequestParameterException("limit")
}

suspend fun RoutingContext.getAuthenticatedUsersId(userRepo: UserRepository): Int? {
    val email: String? = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
    return email?.let { email ->
        userRepo.findByEmail(email).getOrThrow().id
    }
}